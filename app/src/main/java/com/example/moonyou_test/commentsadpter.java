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

public class commentsadpter extends RecyclerView.Adapter<commentsadpter.itemViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;

    private ArrayList<commentsgetset> arrayList;
    private Context context;

    public commentsadpter(ArrayList<commentsgetset> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentsview, parent, false);
        itemViewHolder holder = new itemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        commentsgetset item = arrayList.get(position) ;  // 생성자클래스이름 변수= 리스트명.
        Date write_date = item.getTime(); //작성일을 날짜 타입에 저장
        SimpleDateFormat date = new SimpleDateFormat("MM/dd"); // 시간을 출력할 포맷 설정
        holder.contents.setText(item.getComm());
        holder.writetime.setText(date.format(write_date)); //작성일을 설정한 포맷으로 반환하여 텍스트뷰에 저장
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
                            holder.usernick.setText(user.getNickname());
                        }
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
        TextView usernick;
        TextView writetime;
        TextView contents;
        public itemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.usernick = itemView.findViewById(R.id.writer);
            this.writetime = itemView.findViewById(R.id.writetime);
            this.contents = itemView.findViewById(R.id.commentscontents);
        }
    }
}