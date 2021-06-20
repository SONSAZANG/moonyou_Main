package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class showcommunity extends AppCompatActivity implements communityadaper.OnListItemSelectedInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<mypage_getset> arrayList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcommunity);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        recyclerView = findViewById(R.id.community);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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
                        adapter = new communityadaper(arrayList, showcommunity.this, showcommunity.this::onItemSelected);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
    public void onItemSelected(View v, int pos) {
        communityadaper.communityViewHolder viewHolder = (communityadaper.communityViewHolder)recyclerView.findViewHolderForAdapterPosition(pos);
        mypage_getset item = arrayList.get(pos) ;
        if (pos != RecyclerView.NO_POSITION)
        {
            Intent intent = new Intent(v.getContext(), showcommunity2.class);
            intent.putExtra("show_ID", item.getShow_ID());
            startActivityForResult(intent, 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String callback = data.getStringExtra("callback");
            Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
            outIntent.putExtra("callback", callback);
            setResult(RESULT_OK, outIntent);
            finish();
        }
    }
}