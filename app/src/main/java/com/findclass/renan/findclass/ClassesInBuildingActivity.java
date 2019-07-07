package com.findclass.renan.findclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.findclass.renan.findclass.Adapter.ClassAdapter;
import com.findclass.renan.findclass.Chat.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ClassesInBuildingActivity extends AppCompatActivity {
    FloatingActionButton floatingButton;
    private List<Class> classList;
    private ClassAdapter classAdapter;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_in_building);
        String building_number = getIntent().getStringExtra("building_number");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.building)+building_number+" - "+getString(R.string.classof));

        //Other stuff in OnCreate();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        parseJSONClasses(building_number);

    }

    private void parseJSONClasses(final String building_number) {
        String apiUrl = "http://gsx2json.com/api?id=1UDyRJU39JasISTaA8JfsQmQUT-BWhCMPD3MUWe9Cgs8&sheet="+building_number;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        classList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray rows = response.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        String classNumber = object.getString("class");
                        String vacant = object.getString("vacant");
                        String user = object.getString("user");
                        int fromHour,toHour;
                        fromHour = object.getInt("from1");
                        toHour = object.getInt("to1");
                        if (!((!user.contains("lector")) && checkIfOver(fromHour,toHour,user))) {
                            fromHour = object.getInt("from");
                            toHour = object.getInt("to");
                            user = "lector";
                        }
                        String hours = checkTime(fromHour,toHour);
                        //^^ return the hours if the class is cathed , else return "x"
                        if (hours.contains("x")) vacant ="yes";  // if "x" the class is vacant(empty)
                        else vacant = "no";
                        classList.add(new Class(classNumber,vacant,building_number,hours,user));
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycle2);
                    classAdapter = new ClassAdapter(classList);
                    classAdapter.notifyDataSetChanged();
                    classAdapter.setListener(new ClassAdapter.ClassListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            View parentLayout = findViewById(android.R.id.content);
                            Class classTemp = classList.get(position);
                            String vacant = classTemp.getmVacant();
                            if (firebaseUser != null)
                            {
                                if (vacant.contains("yes") ){
                                    Intent intent= new Intent(ClassesInBuildingActivity.this, ChatActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Snackbar.make(parentLayout, getResources().getString(R.string.must_login), Snackbar.LENGTH_LONG)
                                        .setAction(getResources().getString(R.string.login), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent= new Intent(ClassesInBuildingActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.background_light ))
                                        .show();
                            }
                        }

                        @Override
                        public void onLongItemClicked(int position, View view) {

                        }
                    });
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                    recyclerView.setAdapter(classAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private Boolean checkIfOver(int fromHour, int toHour, String user) {
        //function that checked if the user lector is finished/
        Date date = Calendar.getInstance().getTime();
        int currHour ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH"); //take only the curr hour
        currHour= Integer.parseInt(dateFormat.format(date));
        if(currHour>= toHour || currHour<fromHour){ //check if the curr hour >= finished hour of the class was caught by user
            return false;
        }
        return true;
    }

    private String checkTime(int fromHour, int toHour) {
        Date date = Calendar.getInstance().getTime();
        int currHour ;
        String correctHour;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        currHour= Integer.parseInt(dateFormat.format(date));
        if (fromHour<=currHour && currHour<toHour){
            correctHour = fromHour+":00 - "+toHour+":00";
        }else correctHour = "x";
        return correctHour;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ClassesInBuildingActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
}
