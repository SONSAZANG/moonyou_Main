package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class show_main extends AppCompatActivity implements showNoticeAdapter.OnListItemSelectedInterface{

    private RecyclerView recyclerView;
    private RecyclerView reviewss;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter reviewss_adapter;
    private ArrayList<castgetset> castList;
    private ArrayList<commentsgetset> reviewslist;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView.LayoutManager reviewss_layoutManager1;

    ImageView show_poster;
    TextView title_label;
    TextView period_label;
    TextView runtime_label;
    TextView notice_label;
    show_info show_info;
    String notice;
    StorageReference storageref;
    FirebaseStorage storage;
    EditText review_text;
    String showID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_main);
        Intent intent = getIntent();
        showID = intent.getStringExtra("show_id"); // id가져오기
        Toast.makeText(getApplicationContext(), showID, Toast.LENGTH_SHORT).show();
        recyclerView = findViewById(R.id.recyclerView1);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        recyclerView.setLayoutManager(layoutManager1);
        castList = new ArrayList<>();

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
                            //arrayList.add(show_info);

                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        //adapter = new showNoticeAdapter(arrayList, show_main.this, show_main.this);

                    }
                });
        castList.clear();
        db.collection("show_info")// DB 테이블 연결
                .document(showID)
                .collection("actor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                castgetset cast = document.toObject(castgetset.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                                castList.add(cast); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new castadpter(castList, show_main.this);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
        review_text = findViewById(R.id.reviewtext) ;
        Button button0 = (Button) findViewById(R.id.reviews);
        button0.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!review_text.getText().toString().trim().equals("")) {
                    add_review();
                }
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
        Button button3 = (Button) findViewById(R.id.community);
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
                outIntent.putExtra("callback", "community");
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
        getreviews();
    }

    private void add_review()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long now = System.currentTimeMillis();
        Date date=new Date(now);
        Map<String, Object> comm = new HashMap<>();
        comm.put("uid", user.getUid());
        comm.put("comm", review_text.getText().toString().trim());
        comm.put("time", date);
        db.collection("show_info")
                .document(showID)
                .collection("review")
                .add(comm)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        Log.d("Faberaaaaaaa", "review putting succesfull");
                        Toast.makeText(getApplicationContext(), "리뷰 작성 완료", Toast.LENGTH_LONG).show();;
                        review_text.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(review_text.getWindowToken(), 0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("faberjooaaaaaaa", "Error putting documents: ");
                    }
                });
        getreviews();
    }

    private void getreviews()
    {
        reviewslist = new ArrayList<>();
        reviewslist.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        reviewss = (RecyclerView)findViewById(R.id.reviewss);
        reviewss.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        reviewss_layoutManager1 = new LinearLayoutManager(this);
        reviewss.setLayoutManager(reviewss_layoutManager1);
        db.collection("show_info")
                .document(showID)
                .collection("review")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                commentsgetset comments = document.toObject(commentsgetset.class);
                                Log.d("FABERJOO", String.valueOf(document.getId()));
                                reviewslist.add(comments);
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        reviewss_adapter = new commentsadpter(reviewslist, show_main.this);
                        reviewss.setAdapter(reviewss_adapter); // 리사이클러뷰에 어댑터 연결
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