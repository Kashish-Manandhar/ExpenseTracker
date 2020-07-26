package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IncomeList extends AppCompatActivity{
    Toolbar toolbar;
    RecyclerView recyclerView;
    FirebaseAuth mauth;
    String uid;
    FirebaseFirestore fbfs;
    ListAdapter adapter;
    List<Transaction> transactionList;
    FirebaseFirestore db;
    TextView toalincometV;

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        toolbar=findViewById(R.id.incomelist_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Income List");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toalincometV=findViewById(R.id.totalincometV);

        db=FirebaseFirestore.getInstance();

        recyclerView=findViewById(R.id.incomelist_rec);
        mauth=FirebaseAuth.getInstance();
        uid=mauth.getCurrentUser().getUid();
        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    double totalincome=Double.parseDouble(task.getResult().get("income").toString());
                    toalincometV.setText("Rs "+totalincome);
                }

            }
        });
        fbfs=FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionList=new ArrayList<>();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        fbfs.setFirestoreSettings(settings);


            adapter=new ListAdapter(this,transactionList);
            recyclerView.setAdapter(adapter);
            fbfs.collection("Users").document("Transaction")
                    .collection(uid).orderBy("Millis", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot snapshot:task.getResult())
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(snapshot.getData());
                                String source = jsonObject.getString("Source");
                                double price = jsonObject.getDouble("Price");
                                String time = jsonObject.getString("Time");
                                String date = jsonObject.getString("Date");
                                String type = jsonObject.getString("Type");
                                long milli=jsonObject.getLong("Millis");
                                if(type.equals("income")) {
                                    transactionList.add(new Transaction(source, price, date, time, type,milli));
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    }
            });







    }
}
