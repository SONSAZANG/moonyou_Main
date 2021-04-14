package com.example.moonyou_test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class timeadpter extends RecyclerView.Adapter<timeadpter.itemViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;

    private ArrayList<timegetset> arrayList;
    private Context context;

    public timeadpter(ArrayList<timegetset> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
        holder.Resv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), book_seat.class);
                intent.putExtra("date", item.getDate());
                intent.putExtra("time", item.getTime());
                intent.putExtra("show_id", item.getShow_id());
                v.getContext().startActivity(intent);
            }
        });
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

            Resv.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        // 데이터 리스트로부터 아이템 데이터 참조
                        timegetset item = arrayList.get(pos);
                        Toast.makeText(v.getContext(), item.getTime(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), book_seat.class);
                        intent.putExtra("time", item.getTime());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}