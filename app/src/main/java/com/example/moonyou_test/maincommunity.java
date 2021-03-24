package com.example.moonyou_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
public class maincommunity extends AppCompatActivity
{

    private View navi;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    FrameLayout hot_board;
    FrameLayout notice_board;
    FrameLayout my_board;
    FrameLayout all_board;
    ArrayList<boardgetset> boardlist = new ArrayList<>(); //User 객체를 담을 어레이 리스트결

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincommunity);
        hot_board = (FrameLayout) findViewById(R.id.hot_board);
        notice_board = (FrameLayout) findViewById(R.id.Notice_board);
        my_board = (FrameLayout) findViewById(R.id.my_board);
        all_board = (FrameLayout) findViewById(R.id.all_board);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.bord);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                int pos = tab.getPosition();
                changeView(pos);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        v_fllipper = findViewById(R.id.image_slide2);
        for(int image : images) {
            fllipperImages(image);
        }
    }  //ONCRETE 끝


    private void changeView(int index) {

        switch (index) {
            case 0:
                all_board.setVisibility(View.VISIBLE);
                hot_board.setVisibility(View.GONE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.GONE);
                getboard();
                break;
            case 1:
                all_board.setVisibility(View.GONE);
                hot_board.setVisibility(View.VISIBLE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.GONE);
                break;
            case 2:
                all_board.setVisibility(View.GONE);
                hot_board.setVisibility(View.GONE);
                notice_board.setVisibility(View.VISIBLE);
                my_board.setVisibility(View.GONE);
                break;
            case 3:
                all_board.setVisibility(View.GONE);
                hot_board.setVisibility(View.GONE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.VISIBLE);
                break;
        }
    }

    ViewFlipper v_fllipper;

    int images[] = {
            R.drawable.top_image01,
            R.drawable.top_image02,
            R.drawable.top_image03
    };
    // 이미지 슬라이더 구현 메서드
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }

    private void getboard()
    {
        boardlist.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어 연결
        recyclerView = findViewById(R.id.all_posts);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        db.collection("Board")
                .orderBy("time",Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                boardgetset board = document.toObject(boardgetset.class);
                                board.setId(document.getId());
                                boardlist.add(board);
                                Toast.makeText(getApplicationContext(),document.getId(), Toast.LENGTH_SHORT).show();
                                Log.d("FABERJOO", String.valueOf(board.getdId()));
                            }
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new commadpter(boardlist, maincommunity.this);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }
}
