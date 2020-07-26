package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

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

public class ExpenseList extends AppCompatActivity{
    FirebaseFirestore fbfs;



    FirebaseAuth mauth;
    RecyclerView recyclerView;
    Toolbar mtoolbar;
    String uid;
    List<Transaction> transactionList2;
    ListAdapter adapter;
    FirebaseFirestore db;
    TextView totalexpenseTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        totalexpenseTv=findViewById(R.id.totalexpensetV);
        mtoolbar=findViewById(R.id.expenselist_tool);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Expense List");
        recyclerView=findViewById(R.id.expesnelist_rec);
        mauth=FirebaseAuth.getInstance();
        uid=mauth.getCurrentUser().getUid();
        fbfs=FirebaseFirestore.getInstance();
        db=FirebaseFirestore.getInstance();
        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    double totalexpense=Double.parseDouble(task.getResult().get("expense").toString());
                    totalexpenseTv.setText("Rs "+totalexpense);
                }

            }
        });

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        fbfs.setFirestoreSettings(settings);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionList2=new ArrayList<>();


            adapter = new ListAdapter(this, transactionList2);
            recyclerView.setAdapter(adapter);
            fbfs.collection("Users").document("Transaction")
                    .collection(uid).orderBy("Millis", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            try {
                                JSONObject jsonObject = new JSONObject(snapshot.getData());
                                String source = jsonObject.getString("Source");
                                double price = jsonObject.getDouble("Price");
                                String time = jsonObject.getString("Time");
                                String date = jsonObject.getString("Date");
                                String type = jsonObject.getString("Type");
                                long milli = jsonObject.getLong("Millis");

                                if (type.equals("expense")) {
                                    transactionList2.add(new Transaction(source, price, date, time, type, milli));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            });



    }
}
