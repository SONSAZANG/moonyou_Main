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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class castadpter extends RecyclerView.Adapter<castadpter.itemViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;

    private ArrayList<castgetset> arrayList;
    private Context context;

    public castadpter(ArrayList<castgetset> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.castingitem, parent, false);
        itemViewHolder holder = new itemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        castgetset item = arrayList.get(position) ;  // 생성자클래스이름 변수= 리스트명.
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageref = storage.getReference();
        StorageReference pathReference = storageref.child(item.getActorpath()); //jdk, 3.17 16:30,"position(n번째 이미지뷰) 별 저장소 경로 설정"
        Glide.with(context).load(pathReference).into(holder.CIV);
        holder.showname.setText(item.getActorshowname());
        holder.name.setText(String.valueOf(item.getActorname()));
    }

    @Override
    public int getItemCount()
    {
        return (arrayList != null ? arrayList.size():0);

    }

    public class itemViewHolder extends RecyclerView.ViewHolder
    {
        TextView showname;
        TextView name;
        CircleImageView CIV;
        public itemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.showname = itemView.findViewById(R.id.noticeShowName1);
            this.name = itemView.findViewById(R.id.noticeName1);
            this.CIV = itemView.findViewById(R.id.noticeImage1);
        }
    }
}