package com.example.expensetracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHodler> {
    Context context;
    List<Transaction> transactionList;
    String red="#FF0000";String black="#008000";

    public ListAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ListAdapter.ListViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list,parent,false);
        return new ListViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHodler holder, int position) {
        Transaction transaction=transactionList.get(position);
        if(position!=0)
        {
            if(transaction.getDate().equals(transactionList.get(position-1).getDate()))
            {
                holder.date.setVisibility(View.GONE);
            }
            else
            {
                holder.date.setVisibility(View.VISIBLE);
            }

        }
        else {
            holder.date.setVisibility(View.VISIBLE);
        }



        holder.desc1.setText(transaction.getDescription());
       holder.date.setText(transaction.getDate());
        holder.time1.setText(transaction.getTime());
        if(transaction.getType().equals("income"))
        {
           holder.price1.setText("+"+"Rs "+transaction.getPrice());
           holder.price1.setTextColor(Color.parseColor(black));

        }
        else
        {
            holder.price1.setText("-"+"Rs "+transaction.getPrice());
            holder.price1.setTextColor(Color.parseColor(red));
        }



    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ListViewHodler extends RecyclerView.ViewHolder{
        TextView desc1,price1,time1,date;
        CardView card1;
        public ListViewHodler(@NonNull View itemView) {
            super(itemView);
            desc1=itemView.findViewById(R.id.transdescriptiontv2);
            price1=itemView.findViewById(R.id.transpricetv2);
            time1=itemView.findViewById(R.id.transtime2);
            date=itemView.findViewById(R.id.transdatetv2);
        }
    }
}
