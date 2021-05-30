package com.example.moonyou_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mypage_main extends AppCompatActivity {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<mypage_getset> arrayList;
    private FirebaseFirestore db;
    FirebaseUser user = fAuth.getCurrentUser();
    User info;
    FrameLayout myinfo;
    FrameLayout mypage_info_change;
    FrameLayout myticket;
    FrameLayout mypost;
    String userId;
    EditText username;
    EditText usernick;
    EditText useremail;
    EditText newpwd;
    EditText newpwd1;
    User AAA;
    int nick = 0, email = 0, pwd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_main2);
        userId = user.getUid();
        db = FirebaseFirestore.getInstance();
        myinfo = (FrameLayout) findViewById(R.id.mypage_info);
        mypage_info_change = (FrameLayout) findViewById(R.id.mypage_info_change);
        myticket = (FrameLayout) findViewById(R.id.mypage_ticket);
        mypost = (FrameLayout) findViewById(R.id.mypage_post);
         // 아이디 연결

        TabLayout tabLayout = (TabLayout) findViewById(R.id.myPage);
        myinfo.setVisibility(View.VISIBLE);
        mypage_info_change.setVisibility(View.GONE);
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
        pwd_confirm();
    }

    private void changeView(int index) {

        switch (index) {
            case 0:
                myinfo.setVisibility(View.VISIBLE);
                myticket.setVisibility(View.GONE);
                mypost.setVisibility(View.GONE);
                pwd_confirm();
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

    private void pwd_confirm(){
        EditText beforepwd = (EditText)findViewById(R.id.beforepwd);
        Button pwdconfirm = (Button)findViewById(R.id.pwdconfirm);
        username = (EditText)findViewById(R.id.myid);
        usernick = (EditText)findViewById(R.id.mynick);
        useremail = (EditText)findViewById(R.id.myemail);
        newpwd = (EditText)findViewById(R.id.newpwd);
        newpwd1 = (EditText)findViewById(R.id.newpwd1);
        pwdconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beforepwd.getText().toString().trim().equals(""))
                {
                    db.collection("user")
                            .document(userId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                                    {
                                        DocumentSnapshot document = task.getResult(); //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                                        info = document.toObject(User.class);
                                        if (beforepwd.getText().toString().trim().equals(info.getPassword()))
                                        {
                                            myinfo.setVisibility(View.GONE);
                                            mypage_info_change.setVisibility(View.VISIBLE);
                                            my_info();
                                            beforepwd.setText("");
                                        }
                                        else
                                            {
                                                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void my_info(){
        TextView nick_confirm = (TextView)findViewById(R.id.nick_confirm);
        TextView email_confirm = (TextView)findViewById(R.id.email_confirm);
        TextView pwd_confirm = (TextView)findViewById(R.id.pwd_confirm);
        TextView pwd1_confirm = (TextView)findViewById(R.id.pwd1_confirm);
        Button info_change = (Button)findViewById(R.id.info_cahnge);
        newpwd1.setClickable(false);
        newpwd1.setFocusable(false);
        newpwd1.setFocusableInTouchMode(false);

        usernick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                db.collection("user")
                        .whereEqualTo("nickname", usernick.getText().toString().trim())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                                {
                                    AAA = new User();
                                    for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"결과를  한 줄 씩document에"
                                    {
                                        AAA = document.toObject(User.class);
                                    }
                                }
                                if(usernick.getText().toString().trim().equals(AAA.getNickname()) && !usernick.getText().toString().trim().equals(""))
                                {

                                    nick_confirm.setText("동일한 별명이 존재 합니다.");
                                    nick_confirm.setTextColor(0xaaef4f4f);
                                    if (usernick.getText().toString().trim().equals(info.getNickname()))
                                    {
                                        nick_confirm.setText("변경을 원하시면 별명을 입력하세요.");
                                        nick_confirm.setTextColor(0xff000000);
                                    }
                                    nick = 0;
                                }
                                else
                                {
                                    nick_confirm.setText("사용 가능한 별명입니다.");
                                    nick_confirm.setTextColor(0xdd55ff66);
                                    nick = 1;
                                }
                            }
                        });
                if (nick == 1 || email == 1 || pwd == 1)
                {
                    info_change.setEnabled(true);
                }
                else
                {
                    info_change.setEnabled(false);
                }
            }
        });
        useremail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                db.collection("user")
                        .whereEqualTo("email", useremail.getText().toString().trim())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                                {
                                    AAA = new User();
                                    for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"결과를  한 줄 씩document에"
                                    {
                                        AAA = document.toObject(User.class);
                                    }
                                }
                                if(useremail.getText().toString().trim().equals(AAA.getEmail()) && !useremail.getText().toString().trim().equals(""))
                                {

                                    email_confirm.setText("동일한 이메일 존재 합니다.");
                                    email_confirm.setTextColor(0xaaef4f4f);
                                    if (useremail.getText().toString().trim().equals(info.getEmail()))
                                    {
                                        email_confirm.setText("변경을 원하시면 이메일을 입력하세요.");
                                        email_confirm.setTextColor(0xff000000);
                                    }
                                    email = 0;
                                }
                                else
                                {
                                    email_confirm.setText("사용 가능한 이메일입니다.");
                                    email_confirm.setTextColor(0xdd55ff66);
                                    email = 1;
                                }
                            }
                        });
                if (nick == 1 || email == 1 || pwd == 1)
                {
                    info_change.setEnabled(true);
                }
                else
                {
                    info_change.setEnabled(false);
                }
            }
        });
        newpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                newpwd1.setClickable(false);
                newpwd1.setFocusable(false);
                newpwd1.setFocusableInTouchMode(false);
                pwd_confirm.setTextColor(0xaaef4f4f);
                db.collection("user")
                        .whereEqualTo("password", useremail.getText().toString().trim())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                                {
                                    AAA = new User();
                                    for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"결과를  한 줄 씩document에"
                                    {
                                        AAA = document.toObject(User.class);
                                    }
                                }
                                if(!newpwd.getText().toString().trim().equals(""))
                                {
                                    if (newpwd.getText().toString().trim().length() >= 6)
                                    {
                                        if(!newpwd.getText().toString().trim().equals(info.getPassword()))
                                        {
                                            pwd_confirm.setText("사용 가능한 비밀번호입니다.");
                                            pwd_confirm.setTextColor(0xdd55ff66);
                                            newpwd1.setClickable(true);
                                            newpwd1.setFocusable(true);
                                            newpwd1.setFocusableInTouchMode(true);

                                        }
                                        else
                                        {
                                            pwd_confirm.setText("기존 비밀번호입니다.");
                                        }
                                    }
                                    else{pwd_confirm.setText("비밀번호는 6자리 이상 조합입니다.");}
                                }
                                else {
                                    pwd_confirm.setText("비밀번호를 입력해 주세요.");
                                }
                            }
                        });
            }
        });
        newpwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newpwd.getText().toString().trim().equals(newpwd1.getText().toString().trim()))
                {
                    pwd = 1;
                    pwd1_confirm.setText("비밀번호가 동일합니다.");
                    pwd1_confirm.setTextColor(0xdd55ff66);
                }
                else{
                    pwd1_confirm.setText("비밀번호가 동일하지 않습니다.");
                    pwd1_confirm.setTextColor(0xaaef4f4f);
                    pwd = 0;
                }
                pwd1_confirm.setVisibility(View.VISIBLE);
                if (nick == 1 || email == 1 || pwd == 1)
                {
                    info_change.setEnabled(true);
                }
                else
                {
                    info_change.setEnabled(false);
                }
            }
        });

        db.collection("user")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            DocumentSnapshot document = task.getResult(); //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            info = document.toObject(User.class);
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        username.setText(info.getName());
                        usernick.setText(info.getNickname());
                        useremail.setText(info.getEmail());

                        info_change.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v) {
                                DocumentReference docref = db.collection("user")
                                        .document(userId);
                                Map<String, Object> newinfo = new HashMap<>(); //해쉬맵 선언
                                newinfo.put("nickname", usernick.getText().toString().trim());
                                newinfo.put("email", useremail.getText().toString().trim());
                                newinfo.put("password", newpwd.getText().toString().trim());
                                user.updatePassword(newpwd.getText().toString().trim())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Faber", "User password updated.");
                                                }
                                            }
                                        });
                                user.updateEmail(useremail.getText().toString().trim())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Faber", "User email address updated.");
                                                }
                                            }
                                        });
                                docref.update(newinfo)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("Faber", "Succesfully update");
                                            }
                                        });
                                Toast.makeText(getApplicationContext(), "변경완료", Toast.LENGTH_SHORT).show();
                                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                                outIntent.putExtra("callback", "logout");
                                setResult(RESULT_OK, outIntent);
                                finish();
                            }
                        });
                    }
                });
    }

    private void mycs() {
        recyclerView = findViewById(R.id.REViewMypage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        String userId = user.getUid();
        arrayList = new ArrayList<>(); //jdk, 3.17 16:30,"상태 arraylist 선언"
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
                        mypage_getset.setBook_ID(document.getId());
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