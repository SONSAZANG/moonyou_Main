package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class showcommunity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    FrameLayout behind;
    FrameLayout notice_board;
    FrameLayout my_board;
    FrameLayout all_board;
    FrameLayout navi;
    ArrayList<boardgetset> boardlist = new ArrayList<>();
    ArrayList<boardgetset> noticelist = new ArrayList<>();
    Button writebtn;
    Button home;
    Button mypage;
    boardgetset board;
    String show_id;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcommunity2);
        FirebaseUser user= fAuth.getCurrentUser();
        behind = (FrameLayout) findViewById(R.id.behind);
        notice_board = (FrameLayout) findViewById(R.id.Notice_board);
        my_board = (FrameLayout) findViewById(R.id.my_board);
        all_board = (FrameLayout) findViewById(R.id.all_board);
        navi = (FrameLayout) findViewById(R.id.navi);
        Intent intent1 = getIntent();
        show_id = intent1.getStringExtra("show_ID");


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

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("callback", "home");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

        mypage = findViewById(R.id.mypage_btn);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("callback", "mypage");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        getboard();
    }  //ONCRETE 끝


    private void changeView(int index) {

        switch (index) {
            case 0:
                all_board.setVisibility(View.VISIBLE);
                behind.setVisibility(View.GONE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.GONE);
                navi.setVisibility(View.GONE);
                navi.setVisibility(View.VISIBLE);
                getboard();
                break;
            case 1:
                all_board.setVisibility(View.GONE);
                behind.setVisibility(View.VISIBLE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.GONE);
                navi.setVisibility(View.GONE);
                navi.setVisibility(View.VISIBLE);
                break;
            case 2:
                all_board.setVisibility(View.GONE);
                behind.setVisibility(View.GONE);
                notice_board.setVisibility(View.VISIBLE);
                my_board.setVisibility(View.GONE);
                navi.setVisibility(View.GONE);
                navi.setVisibility(View.VISIBLE);

                break;
            case 3:
                all_board.setVisibility(View.GONE);
                behind.setVisibility(View.GONE);
                notice_board.setVisibility(View.GONE);
                my_board.setVisibility(View.VISIBLE);
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
        db.collection("show_info")
                .document(show_id)
                .collection("show_board")
                .orderBy("time", Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                board = document.toObject(boardgetset.class);
                                board.setdId(document.getId());
                                Log.d("FABERJOO", String.valueOf(board.getdId()));
                                boardlist.add(board);
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new commadpter(boardlist, showcommunity2.this);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }
}



