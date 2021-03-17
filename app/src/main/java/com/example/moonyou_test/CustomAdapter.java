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

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;

    private ArrayList<User> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<User> arrayList, Context context
    , OnListItemSelectedInterface listener
    , OnListItemLongSelectedInterface longListener) {
        this.mListener = listener;
        this.mLongListener = longListener;
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
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_id.setText(arrayList.get(position).getId());
        holder.tv_runtime.setText(String.valueOf(arrayList.get(position).getRuntime()));
        holder.tv_period.setText(arrayList.get(position).getPeriod());



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


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.tv_runtime = itemView.findViewById(R.id.tv_runtime);
            this.tv_period = itemView.findViewById(R.id.tv_period);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });

        }
    }
}
