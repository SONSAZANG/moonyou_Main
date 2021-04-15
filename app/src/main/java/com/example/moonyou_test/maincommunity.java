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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class maincommunity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    FrameLayout hot_board;
    FrameLayout notice_board;
    FrameLayout my_board;
    FrameLayout all_board;
    FrameLayout navi;
    ArrayList<boardgetset> boardlist = new ArrayList<>();
    ArrayList<boardgetset> noticelist = new ArrayList<>();
    Button writebtn;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincommunity);
        hot_board = (FrameLayout) findViewById(R.id.hot_board);
        notice_board = (FrameLayout) findViewById(R.id.Notice_board);
        my_board = (FrameLayout) findViewById(R.id.my_board);
        all_board = (FrameLayout) findViewById(R.id.all_board);
        navi = (FrameLayout) findViewById(R.id.navi);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.bord);

        getboard();
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
        for (int image : images) {
            fllipperImages(image);
        }

        writebtn = findViewById(R.id.write_board);
        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), board_write.class);
                startActivity(intent);
            }
        });

    }  //ONCRETE 끝


    private void changeView(int index) {

        switch (index) {
            case 0:
                all_board.setVisibility(View.VISIBLE);
                hot_board.setVisibility(View.GONE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.GONE);
                navi.setVisibility(View.GONE);
                navi.setVisibility(View.VISIBLE);
                getboard();
                break;
            case 1:
                all_board.setVisibility(View.GONE);
                hot_board.setVisibility(View.VISIBLE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.GONE);
                navi.setVisibility(View.GONE);
                navi.setVisibility(View.VISIBLE);
                break;
            case 2:
                all_board.setVisibility(View.GONE);
                hot_board.setVisibility(View.GONE);
                notice_board.setVisibility(View.VISIBLE);
                getnotice();
                my_board.setVisibility(View.GONE);
                navi.setVisibility(View.GONE);
                navi.setVisibility(View.VISIBLE);

                break;
            case 3:
                all_board.setVisibility(View.GONE);
                hot_board.setVisibility(View.GONE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.VISIBLE);
                getuserwrite();
                navi.setVisibility(View.GONE);
                navi.setVisibility(View.VISIBLE);

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
        v_fllipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    private void getboard() {
        boardlist.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어 연결
        recyclerView = findViewById(R.id.all_posts);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        db.collection("Board")
                .orderBy("time", Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                boardgetset board = document.toObject(boardgetset.class);
                                board.setId(document.getId());
                                Log.d("FABERJOO", String.valueOf(board.getdId()));
                                boardlist.add(board);
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new commadpter(boardlist, maincommunity.this);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }
    private void gethotwrite(){

    }
    private void getnotice() {
        boardlist.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어 연결
        recyclerView = findViewById(R.id.notice_posts);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db.collection("Notice")
                .orderBy("time", Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {

                                boardgetset board = document.toObject(boardgetset.class);
                                board.setId(document.getId());
                                Log.d("FABERJOO", String.valueOf(board.getdId()));
                                noticelist.add(board);
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new noticeAdapter(boardlist, maincommunity.this);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }

    private void getuserwrite() {
        boardlist.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어 연결
        recyclerView = findViewById(R.id.my_posts); //리사이클러뷰아디
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseUser user = fAuth.getCurrentUser(); //"현재 로그인 유저 불러오기"
        String email = user.getEmail(); //"유저 이메일 저장"
        db.collection("user") //user 컬렉션에서
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult())// 16:30,"결과를  한 줄 씩document에"
                            {
                                User name = document.toObject(User.class);
                                String naming = name.getNickname(); //닉네임가져오기
                                Log.d("FABERJOO",naming); //닉네임로그확인

                                db.collection("Board")
                                        .whereEqualTo("username", naming)
                                        //여기서 막힘
                                        .get() //가져오기
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                                                {
                                                    for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                                                    {
                                                        boardgetset board = document.toObject(boardgetset.class);
                                                        board.setId(document.getId());
                                                        Log.d("FABERJOO", document.getId());
                                                        boardlist.add(board);
                                                    }
                                                } else {
                                                    Log.d("faberJOOOOOOO", "Error : ", task.getException());
                                                }
                                                adapter = new commadpter(boardlist, maincommunity.this);
                                                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}



