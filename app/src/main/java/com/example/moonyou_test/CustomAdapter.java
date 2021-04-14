package com.example.moonyou_test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;

    private ArrayList<show_info> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<show_info> arrayList, Context context, OnListItemSelectedInterface listener) {
        this.mListener = listener;
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageref = storage.getReference();
        show_info item = arrayList.get(position) ;  // 생성자클래스이름 변수= 리스트명.
        StorageReference pathReference = storageref.child(item.getImage_Path()); //jdk, 3.17 16:30,"position(n번째 이미지뷰) 별 저장소 경로 설정"
        Glide.with(context).load(pathReference).into(holder.iv_profile);
        holder.tv_id.setText(item.getTitle());
        holder.tv_runtime.setText(String.valueOf(item.getRuntime()));
        holder.tv_period.setText((item.getStartday()) + " ~ " + (item.getFinishday()));
        holder.tv_views.setText(String.valueOf(item.getHit()));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_id;
        TextView tv_runtime;
        TextView tv_period;
        TextView tv_views;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.tv_runtime = itemView.findViewById(R.id.tv_runtime);
            this.tv_period = itemView.findViewById(R.id.tv_period);
            this.tv_views = itemView.findViewById(R.id.tv_views);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }
    }
}
