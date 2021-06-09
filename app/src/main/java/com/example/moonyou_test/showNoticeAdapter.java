package com.example.moonyou_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class showNoticeAdapter extends RecyclerView.Adapter<showNoticeAdapter.CustomViewHolder> {

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private ArrayList<show_info> arrayList;
    private Context context;

    public showNoticeAdapter(ArrayList<show_info> arrayList, Context context, OnListItemSelectedInterface listener) {
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
        Glide.with(context).load(pathReference).into(holder.noticeImage);
        holder.noticeShowName.setText(item.getTitle());
        holder.noticeShowName.setText(String.valueOf(item.getRuntime()));

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView noticeImage;
        TextView noticeShowName;
        TextView noticeName;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.noticeImage = itemView.findViewById(R.id.noticeImage);
            this.noticeShowName = itemView.findViewById(R.id.noticeShowName);
            this.noticeName = itemView.findViewById(R.id.noticeName);


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
