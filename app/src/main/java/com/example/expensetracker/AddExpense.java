package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddExpense extends AppCompatActivity {
    private TextInputLayout descriptionTI,priceTI;
    private Button save;
    private String description,uid;
    private FirebaseAuth mauth;
    private FirebaseFirestore fbfs;
    private double balance,expense,income,price;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        descriptionTI=findViewById(R.id.expense_description);
        priceTI=findViewById(R.id.expense_price);
        save=findViewById(R.id.expense_save);
        toolbar=findViewById(R.id.expense_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()   .setTitle("Add Expense");
        mauth=FirebaseAuth.getInstance();
        uid=mauth.getCurrentUser().getUid();

        fbfs=FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fbfs.setFirestoreSettings(settings);
        final DocumentReference documentReference=fbfs.collection("Users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if(task.isSuccessful())
              {
                  income=Double.parseDouble(task.getResult().get("income").toString());
                  balance=Double.parseDouble(task.getResult().get("balance").toString());
                  expense=Double.parseDouble(task.getResult().get("expense").toString());
              }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description=descriptionTI.getEditText().getText().toString();
                price=Double.parseDouble(priceTI.getEditText().getText().toString());
                if(price>balance)
                {
                    Toast.makeText(AddExpense.this,"Please check the balance.",Toast.LENGTH_SHORT).show();
                    return;



                }
                else {
                    save.setEnabled(false);

                    String date, time;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    date = dateFormat.format(new Date());
                    time = timeFormat.format(new Date());
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    long millis = calendar.getTimeInMillis();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Source", description);
                    hashMap.put("Price", price);
                    hashMap.put("Date", date);
                    hashMap.put("Time", time);
                    hashMap.put("Type", "expense");
                    hashMap.put("Millis", millis);
                    fbfs.collection("Users").document("Transaction").collection(uid).document().set(hashMap).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        expense = expense + price;
                                        balance = income - expense;
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("income", income);
                                        map.put("balance", balance);
                                        map.put("expense", expense);

                                        documentReference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> up_task) {
                                                if (up_task.isSuccessful()) {
                                                    Toast.makeText(AddExpense.this, "Completed Update", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AddExpense.this, HomeActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(AddExpense.this, up_task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                }
            }
        });

    }
}
