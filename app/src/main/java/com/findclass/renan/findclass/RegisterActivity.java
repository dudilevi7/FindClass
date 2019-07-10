package com.findclass.renan.findclass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.findclass.renan.findclass.Adapter.InstitutiosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private String institute ;
    private MaterialEditText username , password, email;
    private Button registerBtn;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;
    private ExpandableListView listView;
    private InstitutiosAdapter institutiosAdapter;
    private List<String> headerList;
    private HashMap<String,List<Integer>> listHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        listView = findViewById(R.id.institutions);
        initData();
        institutiosAdapter = new InstitutiosAdapter(this,headerList,listHashMap);
        listView.setAdapter(institutiosAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (childPosition){
                    case 0:
                        Toast.makeText(getApplicationContext(),getString(R.string.hit),Toast.LENGTH_SHORT).show();
                        institute = "hit";
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),getString(R.string.collegemanage),Toast.LENGTH_SHORT).show();
                        institute = "manage";
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(),getString(R.string.onoacademic),Toast.LENGTH_SHORT).show();
                        institute = "ONO";
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),getString(R.string.tlvacademic),Toast.LENGTH_SHORT).show();
                        institute = "TLV";
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(),getString(R.string.sce),Toast.LENGTH_SHORT).show();
                        institute = "SCE";
                        break;
                }
                return false;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username = findViewById(R.id.username);
        password= findViewById(R.id.password);
        email= findViewById(R.id.email);
        registerBtn= findViewById(R.id.register_btn);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_username = username.getText().toString();
                String input_email = email.getText().toString();
                String input_password = password.getText().toString();
                

                if (TextUtils.isEmpty(input_username)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(input_email)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(input_password)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }else if (input_password.length()<6){
                    Toast.makeText(RegisterActivity.this,getResources().getString(R.string.password_greater_6),Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(institute)){
                    Toast.makeText(RegisterActivity.this,getResources().getString(R.string.select_institute),Toast.LENGTH_SHORT).show();
                }
                else {
                    register(input_username,input_email,input_password);
                }
            }
        });
    }

    private void initData() {
        headerList = new ArrayList<>();
        listHashMap = new HashMap<>();

        headerList.add(getResources().getString(R.string.educational_institutions));
        List<Integer> institutios = new ArrayList<>();
        institutios.add(R.drawable.hit);
        institutios.add(R.drawable.manage_college);
        institutios.add(R.drawable.ono_academic);
        institutios.add(R.drawable.tlv_academic);
        institutios.add(R.drawable.sammyshimon);

        listHashMap.put(headerList.get(0),institutios);
    }

    private void register(final String username , String email, String password)
    {
        dialog.setMessage(getResources().getString(R.string.Loging_please_wait___));
        dialog.setIndeterminate(true);
        dialog.show();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            sendEmailVerification();
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String , String> hashMap = new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");
                            hashMap.put("institute",institute);

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        auth.signOut();
                                        finish();
                                    }
                                }
                            });
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.disconnected), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,getResources().getString(R.string.verification),Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }
            });
        }
    }
}
