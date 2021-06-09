package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class show_main extends AppCompatActivity implements showNoticeAdapter.OnListItemSelectedInterface{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<show_info> arrayList;

    ImageView show_poster;
    TextView title_label;
    TextView period_label;
    TextView runtime_label;
    TextView notice_label;
    show_info show_info;
    String notice;
    StorageReference storageref;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_main);
        Intent intent = getIntent();
        String showID = intent.getStringExtra("show_id"); // id가져오기
        Toast.makeText(getApplicationContext(), showID, Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recyclerView1); // 아이디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //User 객체를 담을 어레이 리스트

        show_poster = findViewById(R.id.show_poster);
        title_label = findViewById(R.id.title_label);
        period_label = findViewById(R.id.period_label);
        runtime_label = findViewById(R.id.runtime_label);
        notice_label = findViewById(R.id.notice);
        notice = getString(R.string.long_text);
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("show_info")
                .document(showID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            DocumentSnapshot document = task.getResult(); //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            show_info = document.toObject(show_info.class);
                            show_info.setShow_id(showID);
                            StorageReference pathReference = storageref.child(show_info.getImage_Path()); //jdk, 3.17 16:30,"position(n번째 이미지뷰) 별 저장소 경로 설정"
                            Glide.with(show_main.this).load(pathReference).into(show_poster);
                            title_label.setText(show_info.getTitle());
                            period_label.setText((show_info.getStartday()) + " ~ " + (show_info.getFinishday()));
                            runtime_label.setText(String.valueOf(show_info.getRuntime()));
                            notice = show_info.getNotice();
                            notice_label.setText(notice);
                            arrayList.add(show_info);

                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new showNoticeAdapter(arrayList, show_main.this, show_main.this);

                    }
                });

        Button button1 = (Button) findViewById(R.id.goticekt);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),book_calender.class);
                intent.putExtra("show_id", show_info.getShow_id());

                startActivityForResult(intent, 1);
            }
        });
        Button button2 = (Button) findViewById(R.id.home);
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
                outIntent.putExtra("callback", "home");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        Button button4 = (Button) findViewById(R.id.mypage_btn);
        button4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
                outIntent.putExtra("callback", "mypage");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        Button button5 = (Button) findViewById(R.id.logout);
        button5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
                outIntent.putExtra("callback", "logout");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String callback = data.getStringExtra("callback");
            Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
            outIntent.putExtra("callback", callback);
            setResult(RESULT_OK, outIntent);
            finish();
        }
    }

    @Override
    public void onItemSelected(View v, int position) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}