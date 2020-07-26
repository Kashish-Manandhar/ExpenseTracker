package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.FirstPartyScopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity implements TransactionAdapter.listener {
    FloatingActionButton fab;
    FirebaseAuth auth;
    Toolbar toolbar;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    String uid,fname;
    TextView tvName,balanceamt,incomeamt,expenseamt;
    ImageView img_add;
    double balance,income,expense;
    private TransactionAdapter adapter;
    List<Transaction> transactionList1;
    FirebaseFirestore fbfs;
     RecyclerView recyclerView;
     TextView income_head;
    @Override
    protected void onStart() {
        super.onStart();
        dispName();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.hometoolbar);
        setSupportActionBar(toolbar);
        auth=FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Home");
        db=FirebaseFirestore.getInstance();
        firebaseUser=auth.getCurrentUser();
        uid=firebaseUser.getUid();
        fbfs=FirebaseFirestore.getInstance();
        tvName=findViewById(R.id.userName);
        balanceamt=findViewById(R.id.balance_amnt);
        incomeamt=findViewById(R.id.incomeamt);
        expenseamt=findViewById(R.id.expenseamt);
        img_add=findViewById(R.id.increase_income);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,AddIncome.class);
                startActivity(intent);
            }
        });

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        fbfs.setFirestoreSettings(settings);


        recyclerView=findViewById(R.id.recyclerView);
        fab=findViewById(R.id.actioin_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionList1=new ArrayList<>();
        adapter=new TransactionAdapter(this,transactionList1);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        displayalllist();








        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(expense>=income)
                {



                }
                else {

                    startActivity(new Intent(HomeActivity.this, AddExpense.class));
                }
                }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.logout:
                auth.signOut();
                gotoHome();
                return true;
            case R.id.list_income:
                   startActivity(new Intent(HomeActivity.this,IncomeList.class));

                //displayincomelist();
                return true;
            case R.id.list_expense:
                startActivity(new Intent(HomeActivity.this,ExpenseList.class));
                return true;
            default:

                return true;
        }

    }

    private void gotoHome() {
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        finish();
    }
    void displayalllist()
    {

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
                            transactionList1.add(new Transaction(source, price, date, time, type,milli));
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


    @Override
    public void onClickListener(final int position) {

        Transaction transaction=transactionList1.get(position);



        long milli=transaction.getMillis();

        CollectionReference collectionReference=fbfs.collection("Users").document("Transaction").collection(uid);

        collectionReference.whereEqualTo("Millis",milli).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    String type;
                    double price;

                    for(QueryDocumentSnapshot snapshot:task.getResult())
                    {
                          type=snapshot.getData().get("Type").toString();
                          price=Double.parseDouble(snapshot.getData().get("Price").toString());

                        final String finalType = type;
                        final double finalPrice = price;
                        snapshot.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful())
                                  {
                                      db.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                  double aft_inc,aft_exp,aft_bal;
                                                  aft_inc=Double.parseDouble(documentSnapshot.get("income").toString());
                                                  aft_exp=Double.parseDouble(documentSnapshot.get("expense").toString());
                                                  aft_bal=Double.parseDouble(documentSnapshot.get("balance").toString());

                                                  if(finalType.equals("income"))
                                                  {
                                                      aft_inc=aft_inc-finalPrice;
                                                      aft_bal=aft_inc-aft_exp;


                                                  }
                                                  else
                                                  {
                                                      aft_exp=aft_exp-finalPrice;
                                                      aft_bal=aft_inc-aft_exp;
                                                  }

                                                  Map<String,Object> map=new HashMap<>();
                                                  map.put("income",aft_inc);
                                                  map.put("expense",aft_exp);
                                                  map.put("balance",aft_bal);

                                                  db.collection("Users").document(uid).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void aVoid) {
                                                            Toast.makeText(HomeActivity.this,"The record is deleted",Toast.LENGTH_SHORT).show();
                                                            dispName();
                                                            transactionList1.clear();
                                                           displayalllist();

                                                      }
                                                  });




                                          }
                                      });

                                  }
                              }
                          });
                    }





                }
            }
            });







    }

    private void dispName()
    {
        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    fname=task.getResult().get("name").toString();
                    balance=Double.parseDouble(task.getResult().get("balance").toString());
                    income=Double.parseDouble(task.getResult().get("income").toString());
                    expense=Double.parseDouble(task.getResult().get("expense").toString());
                    tvName.setText(fname);
                    balanceamt.setText("Rs "+balance+"");
                    incomeamt.setText("Rs "+income+"");
                    expenseamt.setText("Rs "+expense+"");
                }

            }
        });

    }
}
