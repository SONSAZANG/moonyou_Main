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
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "티켓 수령 방법을 선택할 수 있나요?"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "공식 티켓 예매 시 사전 배송(배송비 발생) 또는 현장 수령을 선택할 수 있습니다.\n" +
                "티켓을 미리 배송 받으면 현장에서 티켓 교환 줄을 서지 않고 손목밴드 부스로 바로 가면 되기에 번거로움을 줄일 수 있습니다. 단, 티켓은 분실, 훼손 시 절대 재발급이 되지 않기에 보관에 각별한 유의 부탁드립니다. 또한, 티켓 실물을 현장에 가져오셔야만 입장 손목밴드 착용이 가능합니다.\n" +
                "일정이 임박한 경우 예매처 규정에 따라 티켓 현장 수령만 가능할 수 있습니다."));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "티켓 교환부터 입장까지"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "1. 티켓을 사전 배송 받은 예매자\n" +
                "1일권/2일권 해당 손목밴드 부스(티켓 실물 지참 필수) ▶ 손목밴드 착용 ▶ 입장\n" +
                " \n" +
                "2. 티켓 현장 수령을 선택한 예매자\n" +
                "예매처 확인 후 해당 티켓 부스(예매자 신분증, 예매 내역 지참 필수) ▶ 해당 손목밴드 부스(티켓 실물 지참 필수) ▶ 손목밴드 착용 ▶ 입장\n" +
                " \n" +
                "*손목밴드는 대표자 수령이 불가합니다. 1인당 1장의 티켓을 가지고 직접 손목밴드 부스로 가서 착용하셔야 합니다."));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "티켓만 있으면 입장할 수 있나요?"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "아니요. 티켓을 가지고 손목밴드 부스로 가셔서 손목밴드를 착용한 후에만 입장 가능합니다."));
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


