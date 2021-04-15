package com.example.moonyou_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class mypage_main extends AppCompatActivity {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<mypage_getset> arrayList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_main2);
         // 아이디 연결

        TabLayout tabLayout = (TabLayout) findViewById(R.id.myPage);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                int pos = tab.getPosition(); //jdk, 3.17 16:30,"현재 탭 번호탭 불러오기"
                changeView(pos); //jdk, 3.17 16:30,"탭 번호 변경시 함수 실행"
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


    }

    private void changeView(int index) {
        FrameLayout myinfo = (FrameLayout) findViewById(R.id.mypage_info);
        FrameLayout myticket = (FrameLayout) findViewById(R.id.mypage_ticket);
        FrameLayout mypost = (FrameLayout) findViewById(R.id.mypage_post);

        switch (index) {
            case 0:
                myinfo.setVisibility(View.VISIBLE);
                myticket.setVisibility(View.GONE);
                mypost.setVisibility(View.GONE);
                break;
            case 1:
                myinfo.setVisibility(View.GONE);
                myticket.setVisibility(View.VISIBLE);
                mypost.setVisibility(View.GONE);
                mycs();
                break;
            case 2:
                myinfo.setVisibility(View.GONE);
                myticket.setVisibility(View.GONE);
                mypost.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void mycs() {
        recyclerView = findViewById(R.id.REViewMypage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseUser user = fAuth.getCurrentUser();
        String userId = user.getUid();
        String userEmail = user.getEmail();
        arrayList = new ArrayList<>(); //jdk, 3.17 16:30,"상태 arraylist 선언"
        db = FirebaseFirestore.getInstance();
        arrayList.clear();//jdk, 3.17 16:30,"파이어스토어 연결 "
        db.collection("user")
                .document(userId)
                .collection("resv_his")
                .get()//jdk, 3.17 16:30,"데이터 불러오기"
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) //jdk, 3.17 16:30,"성공시"
                {
                    for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"결과를  한 줄 씩document에"
                    {
                        mypage_getset mypage_getset = document.toObject(mypage_getset.class);
                        mypage_getset.setUser(document.getId());
                        arrayList.add(mypage_getset);
                    }
                }
                else
                {
                    Log.d("faberJOOOOOOO", "Error : ", task.getException());
                }

                adapter = new mypageadapter(arrayList, mypage_main.this);
                recyclerView.setAdapter(adapter);


            }
        });
    }
}