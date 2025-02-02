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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity implements CustomAdapter.OnListItemSelectedInterface{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<show_info> arrayList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_before);

        recyclerView = findViewById(R.id.recyclerView); // 아이디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //User 객체를 담을 어레이 리스트
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
        Date time = new Date();
        String date = format.format(time);
        db = FirebaseFirestore.getInstance(); // 파이어베이스 데이터베이스 연동
        arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
        db.collection("show_info")// DB 테이블 연결
                .orderBy("hit", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                show_info show_info = document.toObject(show_info.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                                if (date.compareTo(show_info.getFinishday()) <= 0)
                                {
                                    show_info.setShow_id(document.getId());
                                    arrayList.add(show_info); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                                }
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new CustomAdapter(arrayList, MainActivity2.this, MainActivity2.this);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });

        Button button2 = (Button) findViewById(R.id.home);
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("callback", "home");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        Button button3 = (Button) findViewById(R.id.community);
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("callback", "community");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        Button button4 = (Button) findViewById(R.id.mypage_btn);
        button4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("callback", "mypage");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        Button button5 = (Button) findViewById(R.id.logout);
        button5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("callback", "logout");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(View v, int position) {
        CustomAdapter.CustomViewHolder viewHolder = (CustomAdapter.CustomViewHolder)recyclerView.findViewHolderForAdapterPosition(position);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageref = storage.getReference();
        show_info item = arrayList.get(position) ;  // 생성자클래스이름 변수= 리스트명.
        StorageReference pathReference = storageref.child(item.getImage_Path()); //jdk, 3.17 16:30,"position(n번째 이미지뷰) 별 저장소 경로 설정"
        ImageView imageView = (ImageView)findViewById(R.id.show_mainImage);
        Glide.with(v.getContext())
                .load(pathReference)
                .into(imageView);

        TextView showName = (TextView)findViewById(R.id.show_name);
        showName.setText(viewHolder.tv_id.getText().toString());

        TextView showPeriod = (TextView)findViewById(R.id.show_period);
        showPeriod.setText(viewHolder.tv_period.getText().toString());

        Button button1 = (Button) findViewById(R.id.showmain_btn);
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),show_main.class);
                intent.putExtra("show_id", item.getShow_id());
                startActivityForResult(intent, 1);
            }
        });
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