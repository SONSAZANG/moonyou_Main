package com.example.moonyou_test;
//고객센터
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.moonyou_test.Login;
import java.util.HashMap;
import java.util.Map;


public class help_center extends AppCompatActivity {
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerview;
    Button QA;
    private RecyclerView recyclerViewlist;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.help_center);
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
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Fruits"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Apple"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Orange"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Banana"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Cars"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Audi"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Aston Martin"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "BMW"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Cadillac"));

        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Places");
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Kerala"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Tamil Nadu"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Karnataka"));
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Maharashtra"));

        data.add(places);

        recyclerview.setAdapter(new ExpandableListAdapter(data)); //어댑터연결

      /*  firebaseFirestore= firebaseFirestore.getInstance();
       recyclerViewlist=findViewById(R.id.recyclerview2);

       Query query= firebaseFirestore.collection("QA");

       firestore<listview> options=new  FirestoreRecyclerOpions*/
        //set the Adapter

        QA = (Button) findViewById(R.id.QA); //1대1문의 버튼
        QA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(help_center.this); //jdk, 3.17 16:30,"경고창 인텐트"
                ad.setTitle("문의내용");       // 제목 //jdk, 3.17 16:30,"경고창 제목 설정 "
                final EditText et = new EditText(help_center.this); //jdk, 3.17 16:30,"경고창 내 텍스트창 선언"
                ad.setView(et); //jdk, 3.17 16:30,"경고창 출력"

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() { //jdk, 3.17 16:30,"YES버튼 클릭 시"
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = et.getText().toString(); //jdk, 3.17 16:30,"경고창 텍스트 창 내의 텍스트 저장"
                        addData(text); //jdk, 3.17 16:30," 저장된 텍스트를 전달하여 db에 저장하는 함수 실행 "
                        mycs(); //jdk, 3.17 16:30,"1:1문의 새로고침"
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() { //jdk, 3.17 16:30,"No 버튼 클릭 시"
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                ad.show();// 창 띄우기
            }

            private void addData(String QA) {
                FirebaseFirestore db = FirebaseFirestore.getInstance(); //jdk, 3.17 16:30,"파이어스토어 연결"
                FirebaseUser user = fAuth.getCurrentUser(); //jdk, 3.17 16:30," 현재 로그인 한 유저 정보 불러오기"
                String email = user.getEmail(); //jdk, 3.17 16:30," 유저의 이메일 저장"
                Map<String, Object> comm = new HashMap<>(); //jdk, 3.17 16:30," 해쉬맵 선언"
                comm.put("QA", QA); //jdk, 3.17 16:30,"QA키에 문의내용 저장"
                comm.put("userid", email); //jdk, 3.17 16:30,"userid키에 유저 이메일 저장"
                comm.put("answer", ""); //jdk, 3.17 16:30,"answer키에 값 없이 저장"
                comm.put("state","답변대기"); //jdk, 3.17 16:30,"state키에 답변상태 저장"
                db.collection("QA") //jdk, 3.17 16:30,"QA 컬렉션에"
                        .add(comm) //jdk, 3.17 16:30,"임의의 문서ID로 데이터 추가"
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) { //jdk, 3.17 16:30,"성공시"
                                Log.d("Faber", "Document ID = " + comm);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("faber", "Document Error!!");
                    }
                });
            }
        });


    }

    private void changeView(int index) {
        FrameLayout FAQ = (FrameLayout) findViewById(R.id.FAQ);
        FrameLayout one_to_one = (FrameLayout) findViewById(R.id.one_to_one);

        switch (index) {
            case 0:
                FAQ.setVisibility(View.VISIBLE);
                one_to_one.setVisibility(View.GONE);
                break;
            case 1:
                FAQ.setVisibility(View.GONE);
                one_to_one.setVisibility(View.VISIBLE);
                mycs();
                break;
        }
    }

    private void mycs() {
        FirebaseUser user = fAuth.getCurrentUser(); //jdk, 3.17 16:30,"현재 로그인 유저 불러오기"
        String email = user.getEmail(); //jdk, 3.17 16:30,"유저 이메일 저장"
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //jdk, 3.17 16:30,"파이어스토어 연결 "
        ArrayList<String> commlist = new ArrayList<String>(); //jdk, 3.17 16:30,"내용 arraylist 선언"
        ArrayList<String> statelist = new ArrayList<String>(); //jdk, 3.17 16:30,"상태 arraylist 선언"
        db.collection("QA") //jdk, 3.17 16:30,"QA 컬렉션에서"
                .whereEqualTo("userid", email) //jdk, 3.17 16:30,"userid 필드 값이 로그인한 유저 이메일과 동일한"
                .get().//jdk, 3.17 16:30,"데이터 불러오기"
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"결과를  한 줄 씩document에"
                            {
                                getset QnA = document.toObject(getset.class); //jdk, 3.17 16:30,"document를 getset클래스 형식으로 QnA에 저장"
                                Log.d("FABERJOOOOOOOO","Hello" + QnA.getState());
                                String comm = QnA.getQA(); //jdk, 3.17 16:30,"문의 내용 comm에 저장"
                                String state = QnA.getState();//jdk, 3.17 16:30,"답변 상태 state에 저장"
                                commlist.add(comm); //jdk, 3.17 16:30,"문의 내용을 arraylist에 추가"
                                statelist.add(state);//jdk, 3.17 16:30,"답변상태를 arraylist에 추가"
                            }
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }

                        RecyclerView recyclerView = findViewById(R.id.recyclerview2); //jdk, 3.17 16:30,"리사이클러 뷰 선언 및 연결"
                        recyclerView.setLayoutManager(new LinearLayoutManager(help_center.this)); //jdk, 3.17 16:30,"리사이클러 뷰의 전체 레이아웃 관리자 선언 및 액티비티 정보 전달"

                        myhelpadapter adapter = new myhelpadapter(commlist, statelist); //jdk, 3.17 16:30,"리사이클러 뷰 데이터 어댑터에 commlist와 statelist 전달"
                        recyclerView.setAdapter(adapter); //jdk, 3.17 16:30,"리사이클러 뷰에 어댑터 설정"
                    }
                });
    }
}


