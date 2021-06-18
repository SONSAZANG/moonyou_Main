package com.example.moonyou_test;
//고객센터
import android.content.DialogInterface;
import android.content.Intent;
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

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "매진되면 티켓을 전혀 구매할 수 없나요?"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "없습니다."));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "현장 판매 진행하나요?"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "매진시 막판 취소표에 따라 진행 여부와 수량을 결정해 5월 9일~10일 경 별도 공지합니다."));

        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "티켓을 배송 받았는데 예매자 신분증이 필요한가");
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "아니요. 배송 받은 티켓을 소지하고 해당 손목밴드 부스로 바로 가시면 됩니다."));

        data.add(places);

        recyclerview.setAdapter(new ExpandableListAdapter(data)); //어댑터연결

      /*  firebaseFirestore= firebaseFirestore.getInstance();
       recyclerViewlist=findViewById(R.id.recyclerview2);

       Query query= firebaseFirestore.collection("QA");

       firestore<listview> options=new  FirestoreRecyclerOpions*/
        //set the Adapter
    }
}


