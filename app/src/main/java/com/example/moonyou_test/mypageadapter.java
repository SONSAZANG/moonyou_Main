package com.example.moonyou_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class mypageadapter extends RecyclerView.Adapter<mypageadapter.mypageViewHolder> {
    private ArrayList<mypage_getset> arrayList;
    private Context context;

    private Uri mImageUri;

    public mypageadapter(ArrayList<mypage_getset> arrayList, Context context) {

        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public mypageViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_myticket, parent, false);
        mypageViewHolder holder = new mypageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull mypageViewHolder holder, int position)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        mypage_getset item = arrayList.get(position);
        holder.myTicketName.setText(item.getTitle());
        holder.myTicketDate.setText(item.getDate());
        holder.myTicketTime.setText(item.getTime());
        String SeatCount = String.valueOf(item.getSeatCount());
        holder.myTicketSeat.setText(item.getSeatName());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class mypageViewHolder extends RecyclerView.ViewHolder {
        ImageView myTicketImage;
        TextView myTicketName;
        TextView myTicketDate;
        TextView myTicketTime;
        TextView myTicketSeat;

        public mypageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.myTicketImage = itemView.findViewById(R.id.myTicketImage);
            this.myTicketName = itemView.findViewById(R.id.myTicketName);
            this.myTicketDate = itemView.findViewById(R.id.myTicketDate);
            this.myTicketTime = itemView.findViewById(R.id.myTicketTime);
            this.myTicketSeat = itemView.findViewById(R.id.myTicketSeat);
        }
    }
}
