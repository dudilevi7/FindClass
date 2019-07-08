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
    private List<String> classNumberList;
    private List<String> fromHourList;
    private List<String> toHourList;
    private List<String> day1List = new ArrayList<>();
    private List<String> day2List= new ArrayList<>();
    private List<String> day3List= new ArrayList<>();
    private List<String> day4List= new ArrayList<>();
    private List<String> day5List= new ArrayList<>();
    private List<String> day6List= new ArrayList<>();
    private ClassAdapter classAdapter;
    private FirebaseUser firebaseUser;
    private String adminId = "mjh3M3yH63YnWd6cSyY83a8uKL03";

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
        //String apiUrl = "http://gsx2json.com/api?id=1UDyRJU39JasISTaA8JfsQmQUT-BWhCMPD3MUWe9Cgs8&sheet="+building_number;
        String apiUrl = "http://gsx2json.com/api?id=1We5BBQJykfnN9hITfNO0VVI60SboMXEjktpGBrpCuZg&sheet="+building_number+"&q="+cuurTime();
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
                        String fromHour = object.getString("from");
                        String toHour = object.getString("to");
                        String day = object.getString("day"+currDay()); //users in current day
                        boolean vacant;
                        if (day.contains("0")) vacant = true;
                        else vacant = false;
                        classList.add(new Class(classNumber,vacant,building_number,fromHour+" - "+toHour,day));
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycle2);
                    classAdapter = new ClassAdapter(classList);
                    classAdapter.notifyDataSetChanged();
                    classAdapter.setListener(new ClassAdapter.ClassListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            View parentLayout = findViewById(android.R.id.content);
                            Class classTemp = classList.get(position);
                            if (firebaseUser != null)
                            {
                                if (!firebaseUser.getUid().equals(adminId))
                                {
                                    Intent intent= new Intent(ClassesInBuildingActivity.this, MessageActivity.class);
                                    intent.putExtra("userid",adminId);
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


    private int currDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day;
    }

    private String cuurTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        int currHour= Integer.parseInt(dateFormat.format(date));
        return currHour + "";
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
        currHour = Integer.parseInt(dateFormat.format(date));
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
