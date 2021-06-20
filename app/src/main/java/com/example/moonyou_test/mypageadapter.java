package com.example.moonyou_test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class mypageadapter extends RecyclerView.Adapter<mypageadapter.mypageViewHolder> {
    public interface OnbItemSelectedInterface {
        void onbItemSelected(View v, int position);
    }
    private mypageadapter.OnbItemSelectedInterface mListener;
    private ArrayList<mypage_getset> arrayList;
    private Context context;
    StorageReference storageref;
    FirebaseStorage storage;
    private Uri mImageUri;

    public mypageadapter(ArrayList<mypage_getset> arrayList, Context context, mypageadapter.OnbItemSelectedInterface listener) {

        this.arrayList = arrayList;
        this.context = context;
        this.mListener = listener;
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
        mypage_getset item = arrayList.get(position);
        holder.myTicketID.setText(item.getBook_ID());
        holder.myTicketName.setText(item.getTitle());
        holder.myTicketDate.setText(item.getDate());
        holder.myTicketTime.setText(item.getTime());
        holder.myTicketSeat.setText(item.getSeatName());
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();
        StorageReference pathReference = storageref.child(item.getImage());
        Glide.with(holder.itemView).load(pathReference).into(holder.myTicketImage);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class mypageViewHolder extends RecyclerView.ViewHolder {
        ImageView myTicketImage;
        TextView myTicketID;
        TextView myTicketName;
        TextView myTicketDate;
        TextView myTicketTime;
        TextView myTicketSeat;

        public mypageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.myTicketImage = itemView.findViewById(R.id.myTicketImage);
            this.myTicketID = itemView.findViewById(R.id.TicketID);
            this.myTicketName = itemView.findViewById(R.id.myTicketName);
            this.myTicketDate = itemView.findViewById(R.id.myTicketDate);
            this.myTicketTime = itemView.findViewById(R.id.myTicketTime);
            this.myTicketSeat = itemView.findViewById(R.id.myTicketSeat);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    mListener.onbItemSelected(v, getAdapterPosition());
                }
            });
        }
    }
}