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
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;

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
                AlertDialog.Builder ad = new AlertDialog.Builder(help_center.this);
                ad.setTitle("문의내용");       // 제목
                final EditText et = new EditText(help_center.this);
                ad.setView(et);

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = et.getText().toString();
                        addData(text);
                        mycs();
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                ad.show();// 창 띄우기
            }

            private void addData(String QA) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = fAuth.getCurrentUser();
                String email = user.getEmail();
                Map<String, Object> comm = new HashMap<>();
                comm.put("QA", QA);
                comm.put("userid", email);
                comm.put("answer", "");
                comm.put("state","답변대기");
                db.collection("QA")
                        .add(comm)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
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
        FirebaseUser user = fAuth.getCurrentUser();
        String email = user.getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> commlist = new ArrayList<String>();
        ArrayList<String> statelist = new ArrayList<String>();
        db.collection("QA")
                .whereEqualTo("userid", email)
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                getset QnA = document.toObject(getset.class);
                                Log.d("FABERJOOOOOOOO","Hello" + QnA.getState());
                                String comm = QnA.getQA();
                                String state = QnA.getState();
                                commlist.add(comm);
                                statelist.add(state);
                            }
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }

                        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
                        recyclerView.setLayoutManager(new LinearLayoutManager(help_center.this));

                        myhelpadapter adapter = new myhelpadapter(commlist, statelist);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}


