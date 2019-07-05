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
import com.google.firebase.auth.FirebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_in_building);
        String building_number = getIntent().getStringExtra("building_number");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.building)+building_number+" - "+getString(R.string.classof));


        floatingButton = findViewById(R.id.chat_btn);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ClassesInBuildingActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
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
                        int fromHour = object.getInt("from");
                        int toHour = object.getInt("to");
                        String hours = checkTime(fromHour,toHour);
                        if (hours.contains("x")) vacant ="yes";
                        else vacant = "no";
                        classList.add(new Class(classNumber,vacant,building_number,hours));
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycle2);
                    classAdapter = new ClassAdapter(classList);
                    classAdapter.notifyDataSetChanged();
                    classAdapter.setListener(new ClassAdapter.ClassListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            Class classTemp = classList.get(position);
                            String vacant = classTemp.getmVacant();
                            if (vacant.contains("yes")){
                                Intent intent= new Intent(ClassesInBuildingActivity.this, ChatActivity.class);
                                startActivity(intent);
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
