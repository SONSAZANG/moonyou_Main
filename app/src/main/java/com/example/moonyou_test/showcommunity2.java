package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class showcommunity2 extends AppCompatActivity implements commadpter.OnAItemSelectedInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    FrameLayout behind;
    FrameLayout notice_board;
    FrameLayout my_board;
    FrameLayout all_board;
    FrameLayout navi;
    ArrayList<boardgetset> boardlist = new ArrayList<>();
    Button writebtn;
    Button home;
    Button mypage;
    Button logout;
    boardgetset board;
    String show_id, a;
    TextView show_name;
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
        show_name = (TextView)findViewById(R.id.show_name);
        Intent intent1 = getIntent();
        show_id = intent1.getStringExtra("show_ID");
        a = "1";


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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("show_info")
                .document(show_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            DocumentSnapshot document = task.getResult(); //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            show_info show_info = document.toObject(show_info.class);
                            show_name.setText(show_info.getTitle());
                            //arrayList.add(show_info);

                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        //adapter = new showNoticeAdapter(arrayList, show_main.this, show_main.this);

                    }
                });

        writebtn = findViewById(R.id.write_board);
        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), board_write2.class);
                intent.putExtra("show_id", show_id);
                intent.putExtra("Boardtype", a);
                startActivityForResult(intent, 2);
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), showcommunity.class);
                outIntent.putExtra("callback", "home");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

        mypage = findViewById(R.id.mypage_btn);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), showcommunity.class);
                outIntent.putExtra("callback", "mypage");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getApplicationContext(), showcommunity.class);
                outIntent.putExtra("callback", "home");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }  //ONCRETE 끝


    private void changeView(int index) {

        switch (index) {
            case 0:
                a = "1";
                getboard();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                a = "4";
                getuserwrite();
                break;
        }
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
                .collection("board")
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
                        adapter = new commadpter(boardlist, showcommunity2.this, showcommunity2.this::onAItemSelected);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }

    private void getuserwrite() {
        boardlist.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어 연결
        recyclerView = findViewById(R.id.all_posts);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseUser user = fAuth.getCurrentUser(); //"현재 로그인 유저 불러오기"
        String Uid = user.getUid(); //"유저 이메일 저장"
        db.collection("show_info")
                .document(show_id)
                .collection("board")
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
                                if(board.getUid().equals(Uid))
                                {
                                    board.setdId(document.getId());
                                    Log.d("FABERJOO", String.valueOf(board.getdId()));
                                    boardlist.add(board);
                                }
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new commadpter(boardlist, showcommunity2.this, showcommunity2.this::onAItemSelected);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }

    public void onAItemSelected(View v, int pos) {
        commadpter.itemViewHolder viewHolder = (commadpter.itemViewHolder)recyclerView.findViewHolderForAdapterPosition(pos);
        boardgetset item = boardlist.get(pos) ;FirebaseFirestore db = FirebaseFirestore.getInstance(); //jdk, 3.17 16:30,"파이어스토어 연결"
        DocumentReference docref = db.collection("show_info")
                .document(show_id)
                .collection("board")
                .document(item.getdId());
        Map<String, Object> view = new HashMap<>(); //해쉬맵 선언
        view.put("views", item.getViews() + 1);
        docref.update(view)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Faber", "Succesfully deleted");
                    }
                });
        Intent intent = new Intent(v.getContext(), showcommunity3.class);
        intent.putExtra("BoardID", item.getdId());
        intent.putExtra("showID", show_id);
        intent.putExtra("Boardtype", a);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String callback, Boardtype;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            callback = data.getStringExtra("callback");
            Boardtype = data.getStringExtra("Boardtype");
            switch (callback){
                case "writesucess":
                case "refresh":
                    switch (Boardtype) {
                        case "1":
                            getboard();
                            break;
                        case "4":
                            getuserwrite();
                            break;
                    }
                    break;
                case "home":
                case "mypage":
                case "logout":
                    callback = data.getStringExtra("callback");
                    Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                    outIntent.putExtra("callback", callback);
                    setResult(RESULT_OK, outIntent);
                    finish();
            }
        }
    }
}