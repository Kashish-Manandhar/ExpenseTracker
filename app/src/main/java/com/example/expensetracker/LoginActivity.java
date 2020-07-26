package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView tV_forgotpw;
    Button login;
    FirebaseAuth auth;
    TextInputLayout Email,Password;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email=findViewById(R.id.login_email);
        Password=findViewById(R.id.login_password);
        tV_forgotpw=findViewById(R.id.forgotpw);
        login=findViewById(R.id.loginBtn);

        tV_forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));


            }
        });



        auth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=Email.getEditText().getText().toString().trim();
                password=Password.getEditText().getText().toString().trim();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful())
                         {
                             startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                             finish();
                         }
                         else
                         {
                             Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                         }
                    }
                });
            }
        });

    }
}
