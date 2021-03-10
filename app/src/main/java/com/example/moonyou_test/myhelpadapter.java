package com.example.moonyou_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myhelpadapter extends RecyclerView.Adapter<myhelpadapter.ViewHolder> {

    private ArrayList<String> commData = null;
    private ArrayList<String> stateData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commtv;
        TextView statetv;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            commtv = itemView.findViewById(R.id.comm);
            statetv = itemView.findViewById(R.id.state);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    myhelpadapter(ArrayList<String> comm, ArrayList<String> state) {
        commData = comm;
        stateData = state;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.myhelpview, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String commtext = commData.get(position) ;
        String statetext = stateData.get(position) ;
        holder.commtv.setText(commtext) ;
        holder.statetv.setText(statetext);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return commData.size() ;
    }
}