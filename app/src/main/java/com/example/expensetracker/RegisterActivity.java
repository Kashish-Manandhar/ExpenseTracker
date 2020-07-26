package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextView tVregHead, login;
    TextInputLayout Name, Email, Password;
    Button register;
    FirebaseAuth mauth;
    String name, password, email;
    FirebaseFirestore db;
    User app_user;
     float initial=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tVregHead = findViewById(R.id.register_Head);
        tVregHead.setText("Lets Get" + "\n" + "On Board");
        Name = findViewById(R.id.registerName);
        Email = findViewById(R.id.registerEmail);
        Password = findViewById(R.id.registerPassword);
        register = findViewById(R.id.registerBtn);
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Name.getEditText().getText().toString().trim();
                email = Email.getEditText().getText().toString().trim();
                password = Password.getEditText().getText().toString().trim();
                app_user=new User(name,email,password,initial,initial,initial);

                mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           Log.d("User", "onComplete: "+mauth.getCurrentUser().getUid());
                           db.collection("Users").document(mauth.getCurrentUser().getUid()).set(app_user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                     Toast.makeText(RegisterActivity.this,"Ok .. done",Toast.LENGTH_SHORT).show();
                                     Log.d("Database","Created");
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.d("MyError", "onFailure: " +e.getMessage());
                               }
                           });
                           startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                           finish();
                       }
                       else
                       {
                           Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                       }
                    }
                });
            }
    });
      }
    }

