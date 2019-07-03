package com.findclass.renan.findclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    private MaterialEditText passwordEt, emailEt;
    private Button loginBtn;
    private TextView forgotPasswordTv;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        passwordEt = findViewById(R.id.password);
        emailEt = findViewById(R.id.email);
        loginBtn = findViewById(R.id.login_btn);
        forgotPasswordTv = findViewById(R.id.forgot_password_tv);
        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        dialog = new ProgressDialog(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_email = emailEt.getText().toString();
                String input_password = passwordEt.getText().toString();

                if (TextUtils.isEmpty(input_email)){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(input_password)){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }else if (input_password.length()<6){
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.password_greater_6),Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    dialog.setMessage(getResources().getString(R.string.Loging_please_wait___));
                    dialog.setIndeterminate(true);
                    dialog.show();
                    auth.signInWithEmailAndPassword(input_email,input_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                checkIfEmailVerified();
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.Login_Failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified(){
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        if(!emailVerified){
            Toast.makeText(this,"Verify the Email Id",Toast.LENGTH_SHORT).show();
            auth.signOut();
            finish();
        }
        else {
            emailEt.getText().clear();
            passwordEt.getText().clear();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
