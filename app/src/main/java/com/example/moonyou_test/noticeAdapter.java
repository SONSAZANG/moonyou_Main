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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.itemViewHolder> {

        private ArrayList<boardgetset> arrayList;
        private Context context;

        public noticeAdapter(ArrayList<boardgetset> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }
        @NonNull
        @Override
    public noticeAdapter.itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notice, parent, false);
            noticeAdapter.itemViewHolder holder = new noticeAdapter.itemViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull noticeAdapter.itemViewHolder holder, int position)
        {
            boardgetset item = arrayList.get(position) ;  // 생성자클래스이름 변수= 리스트명.
            Date write_date = item.getTime(); //작성일을 날짜 타입에 저장
            SimpleDateFormat date = new SimpleDateFormat("MM/dd"); // 시간을 출력할 포맷 설정
            holder.boardtitle.setText(item.getTitle());
            holder.boardviews.setText(String.valueOf(item.getViews()));
            holder.boarddate.setText(date.format(write_date)); //작성일을 설정한 포맷으로 반환하여 텍스트뷰에 저장
        }
        @Override
        public int getItemCount()
        {
            return (arrayList != null ? arrayList.size():0);
        }
        public class itemViewHolder extends RecyclerView.ViewHolder
        {
            TextView boardtitle;
            TextView boardviews;
            TextView boarddate;

            public itemViewHolder(@NonNull View itemView)
            {
                super(itemView);
                this.boardtitle = itemView.findViewById(R.id.noticetitle);
                this.boardviews = itemView.findViewById(R.id.noticeviews);
                this.boarddate = itemView.findViewById(R.id.noticedate);
            }
        }
    }

