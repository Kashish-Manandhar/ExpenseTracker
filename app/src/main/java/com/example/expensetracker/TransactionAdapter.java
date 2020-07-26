package com.example.expensetracker;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.HeaderViewHolder>{
    Context context;
    List<Transaction> transactionList;

    public interface listener{
        void onClickListener(int position);
    }

    String red="#FF0000";
    listener Listener;
    String black="#008000";



    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
        this.Listener=(listener)context;



    }


    @NonNull
    @Override
    public TransactionAdapter.HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
            return new HeaderViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionAdapter.HeaderViewHolder holder, final int position) {
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



            ((HeaderViewHolder) holder).desc1.setText(transaction.getDescription());
            ((HeaderViewHolder) holder).date.setText(transaction.getDate());
            ((HeaderViewHolder) holder).time1.setText(transaction.getTime());
            if(transaction.getType().equals("income"))
            {
                ((HeaderViewHolder) holder).price1.setText("+"+"Rs "+transaction.getPrice());
                ((HeaderViewHolder) holder).price1.setTextColor(Color.parseColor(black));

            }
            else
            {
                ((HeaderViewHolder) holder).price1.setText("-"+"Rs "+transaction.getPrice());
                holder.price1.setTextColor(Color.parseColor(red));
            }






    }

    @Override
    public int getItemCount() {

        return transactionList.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
          TextView desc1,price1,time1,date;
          CardView card1;
          ImageView delete;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            desc1=itemView.findViewById(R.id.transdescriptiontv1);
            price1=itemView.findViewById(R.id.transpricetv1);
            time1=itemView.findViewById(R.id.transtime1);
            date=itemView.findViewById(R.id.transdatetv);
            card1=itemView.findViewById(R.id.exp_layout1);

                 delete = itemView.findViewById(R.id.delete);

                 delete.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Listener.onClickListener(getAdapterPosition());
                     }
                 });




        }
    }

}
