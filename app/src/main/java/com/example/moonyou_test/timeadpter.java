package com.example.moonyou_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.moonyou_test.MainActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class timeadpter extends RecyclerView.Adapter<timeadpter.itemViewHolder> {

    public interface OnListItemSelectedInterface {
        void onListItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;

    private ArrayList<timegetset> arrayList;
    private Context context;

    public timeadpter(ArrayList<timegetset> arrayList, Context context, OnListItemSelectedInterface listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable, parent, false);
        itemViewHolder holder = new itemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position)
    {
        timegetset item = arrayList.get(position) ;  // 생성자클래스이름 변수= 리스트명.
        holder.time.setText(item.getTime());
        holder.left_seat.setText(String.valueOf(item.getLeft_seat()) + "/" + String.valueOf(item.getTotal_seat()));
    }

    @Override
    public int getItemCount()
    {
        return (arrayList != null ? arrayList.size():0);

    }

    public class itemViewHolder extends RecyclerView.ViewHolder
    {
        TextView time;
        TextView left_seat;
        Button Resv;


        public itemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.time = itemView.findViewById(R.id.time);
            this.left_seat = itemView.findViewById(R.id.leftseat);
            this.Resv = itemView.findViewById(R.id.resv);
            this.Resv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onListItemSelected(v, getAdapterPosition());
                }
            });
        }
    }
}