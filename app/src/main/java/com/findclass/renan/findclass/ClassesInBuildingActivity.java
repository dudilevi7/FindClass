package com.findclass.renan.findclass;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.findclass.renan.findclass.Adapter.ScheduleAdapter;
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

    private List<Class> classList;
    private ArrayList<Schedule> scheduleArrayList;
    private ScheduleAdapter scheduleAdapter;
    private ClassAdapter classAdapter;
    private FirebaseUser firebaseUser;
    private String adminId = "n4XdU5nhXEdCFBN0byQs7NtWRO93";
    private ProgressBar progressBar,progressBarDialog;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_in_building);
        String building_number = getIntent().getStringExtra("building_number");
        progressBar = findViewById(R.id.progress_classes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.building)+building_number+" - "+getString(R.string.classof));

        //Other stuff in OnCreate();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser==null) flag = false;
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
                    progressBar.setVisibility(View.GONE);
                    classAdapter.setListener(new ClassAdapter.ClassListener() {
                        @Override
                        public void onItemClicked(int position, View view) {

                            View parentLayout = findViewById(android.R.id.content);
                            Class classTemp = classList.get(position);
                            //if (classTemp.getmVacant()) //the class is empty
                            if (firebaseUser != null)
                            {
                                startExtendedView(classTemp, building_number);
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

    private void startExtendedView(Class classTemp,String building_number) {
        final Dialog dialog = new Dialog(ClassesInBuildingActivity.this);
        dialog.setContentView(R.layout.list_schedule);
        progressBarDialog = dialog.findViewById(R.id.progress_dialog);
        final TextView classNoTv, dateTv, chatTv;
        String apiUrl = "http://gsx2json.com/api?id=1We5BBQJykfnN9hITfNO0VVI60SboMXEjktpGBrpCuZg&sheet="+building_number+"&q="+classTemp.getmClassNumber();
        classNoTv = dialog.findViewById(R.id.class_num_tv);
        dateTv = dialog.findViewById(R.id.day_date_tv);
        chatTv = dialog.findViewById(R.id.button_chat);
        if (!classTemp.getmVacant() || firebaseUser.getUid().equals(adminId)) chatTv.setVisibility(View.GONE);
       // floatingActionButton = dialog.findViewById(R.id.fab);
        classNoTv.setText(getString(R.string.classes)+" "+classTemp.getmClassNumber());
        dateTv.setText(getDate());
        scheduleArrayList = new ArrayList<>();
        //parsing JSON
        RequestQueue requestQueue = Volley.newRequestQueue(dialog.getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray rows = response.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        String fromHour = object.getString("from");
                        String toHour = object.getString("to");
                        String day = object.getString("day"+currDay()); //users in current day
                        String hour = fromHour+" - "+toHour;
                        if (day.contains("0")) day =""; //0 is case of empty
                        scheduleArrayList.add(new Schedule(hour,day));
                    }
                    scheduleAdapter = new ScheduleAdapter(scheduleArrayList,getApplicationContext());
                    scheduleAdapter.notifyDataSetChanged();
                    ListView listView = dialog.findViewById(R.id.list_schedule);
                    progressBarDialog.setVisibility(View.GONE);
                    listView.setAdapter(scheduleAdapter);
                    chatTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent= new Intent(ClassesInBuildingActivity.this, MessageActivity.class);
                                intent.putExtra("userid",adminId);
                                startActivity(intent);
                        }
                    });
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

        dialog.show();
    }

    private String getDate() {
        Date date = Calendar.getInstance().getTime();
        String today = date.toString();
        //EEEEE MMMMM yyyy HH:mm:ss
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
        today=dateFormat.format(date);
        return today;
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
        if (currHour < 10) return "0"+currHour+":00";
        return currHour + ":00";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.chat:
                intent = new Intent(ClassesInBuildingActivity.this, ChatActivity.class);
                if (flag)
                startActivity(intent);
                else Toast.makeText(getApplicationContext(),getString(R.string.must_login2),Toast.LENGTH_SHORT).show();
                //finish();
                return true;
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(ClassesInBuildingActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
}
