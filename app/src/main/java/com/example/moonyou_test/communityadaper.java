package com.example.moonyou_test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class communityadaper extends RecyclerView.Adapter<communityadaper.communityViewHolder> {
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private communityadaper.OnListItemSelectedInterface mListener;
    private ArrayList<mypage_getset> arrayList;
    private Context context;
    StorageReference storageref;
    FirebaseStorage storage;
    private Uri mImageUri;

    public communityadaper(ArrayList<mypage_getset> arrayList, Context context, communityadaper.OnListItemSelectedInterface listener) {

        this.arrayList = arrayList;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public communityViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_myticket, parent, false);
        communityViewHolder holder = new communityViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull communityViewHolder holder, int position)
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

    public class communityViewHolder extends RecyclerView.ViewHolder {
        ImageView myTicketImage;
        TextView myTicketID;
        TextView myTicketName;
        TextView myTicketDate;
        TextView myTicketTime;
        TextView myTicketSeat;

        public communityViewHolder(@NonNull View itemView) {
            super(itemView);
            this.myTicketImage = itemView.findViewById(R.id.myTicketImage);
            this.myTicketID = itemView.findViewById(R.id.TicketID);
            this.myTicketName = itemView.findViewById(R.id.myTicketName);
            this.myTicketDate = itemView.findViewById(R.id.myTicketDate);
            this.myTicketTime = itemView.findViewById(R.id.myTicketTime);
            this.myTicketSeat = itemView.findViewById(R.id.myTicketSeat);
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