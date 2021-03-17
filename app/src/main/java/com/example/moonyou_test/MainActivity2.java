package com.example.moonyou_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements CustomAdapter.OnListItemLongSelectedInterface
        , CustomAdapter.OnListItemSelectedInterface{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_before);

        recyclerView = findViewById(R.id.recyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //User 객체를 담을 어레이 리스트

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("User"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비

                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new CustomAdapter(arrayList, this, this, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결




    }

    @Override
    public void onItemLongSelected(View v, int position) {
        Toast.makeText(this, position + "long clicked", Toast.LENGTH_SHORT).show(); // 길게 누를시 안내
    }

    @Override
    public void onItemSelected(View v, int position) {
        CustomAdapter.CustomViewHolder viewHolder = (CustomAdapter.CustomViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
        Toast.makeText(this, viewHolder.tv_id.getText().toString(),Toast.LENGTH_SHORT).show(); // 짧게 누를시 안내



        ImageView imageView = (ImageView)findViewById(R.id.show_mainImage);
        Glide.with(viewHolder.iv_profile)
                .load(arrayList.get(position).getProfile())
                .into(imageView);

        TextView showName = (TextView)findViewById(R.id.show_name);
        showName.setText(viewHolder.tv_id.getText().toString());

        TextView showPeriod = (TextView)findViewById(R.id.show_period);
        showPeriod.setText(viewHolder.tv_period.getText().toString());

        Button button1 = (Button) findViewById(R.id.showmain_btn);
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),show_main.class);
                intent.putExtra("showName", viewHolder.tv_id.getText().toString());
                intent.putExtra("showPeriod", viewHolder.tv_period.getText().toString());
                intent.putExtra("showImage", viewHolder.iv_profile.toString());

                startActivity(intent);
            }
        });

    }
}