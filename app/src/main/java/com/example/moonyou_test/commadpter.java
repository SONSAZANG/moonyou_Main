package com.example.moonyou_test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class commadpter extends RecyclerView.Adapter<commadpter.itemViewHolder> {


    public interface OnAItemSelectedInterface {
        void onAItemSelected(View v, int position);
    }

    private OnAItemSelectedInterface mListener;

    private ArrayList<boardgetset> arrayList;
    private Context context;

    public commadpter(ArrayList<boardgetset> arrayList, Context context, commadpter.OnAItemSelectedInterface listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boardview, parent, false);
        itemViewHolder holder = new itemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        boardgetset item = arrayList.get(position) ;  // 생성자클래스이름 변수= 리스트명.
        Date write_date = item.getTime(); //작성일을 날짜 타입에 저장
        SimpleDateFormat date = new SimpleDateFormat("MM/dd"); // 시간을 출력할 포맷 설정
        holder.boardtitle.setText(item.getTitle());
        holder.boardcomments.setText("[" + String.valueOf(item.getComments()) + "]");
        holder.boarddate.setText(date.format(write_date)); //작성일을 설정한 포맷으로 반환하여 텍스트뷰에 저장
        db.collection("user")
                .document(item.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            User user = document.toObject(User.class);
                            holder.username.setText(user.getNickname());
                        }
                    }
                });
        holder.views.setText(String.valueOf(item.getViews()));
    }

    @Override
    public int getItemCount()
    {
        return (arrayList != null ? arrayList.size():0);

    }

    public class itemViewHolder extends RecyclerView.ViewHolder
    {
        TextView boardtitle;
        TextView boardcomments;
        TextView boarddate;
        TextView username;
        TextView views;


        public itemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.boardtitle = itemView.findViewById(R.id.boardtitle);
            this.boardcomments = itemView.findViewById(R.id.boardcomments);
            this.boarddate = itemView.findViewById(R.id.boarddate);
            this.username = itemView.findViewById(R.id.username);
            this.views = itemView.findViewById(R.id.views);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        mListener.onAItemSelected(v, getAdapterPosition());
                        // 데이터 리스트로부터 아이템 데이터 참조
                    }
                }
            });
        }
    }
}