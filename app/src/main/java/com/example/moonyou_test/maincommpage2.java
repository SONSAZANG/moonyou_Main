package com.example.moonyou_test;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class maincommpage2 extends AppCompatActivity{
    //객체 선언
    EditText comment_text;
    String BoardID;
    int comments_value;
    commentsgetset comments;
    ArrayList<commentsgetset> commentslist = new ArrayList<>();
    RecyclerView commentss;
    RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView dt_title;
    TextView dt_uname;
    TextView dt_time;
    WebView dt_write;
    TextView dt_views;
    TextView dt_comments;
    TextView dt_comments1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincommpage2);

        Intent intent = getIntent();
        BoardID = intent.getStringExtra("BoardID");

        Button create_comment = (Button) findViewById(R.id.create_comment);
        create_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment_text.getText().toString().trim().equals("")) {
                    add_comment();
                }
            }
        });
        dt_title = (TextView) findViewById(R.id.detail_title);
        dt_uname = (TextView) findViewById(R.id.detail_username);
        dt_time = (TextView) findViewById(R.id.detail_time);
        dt_write = findViewById(R.id.webView);
        dt_views = (TextView) findViewById(R.id.detail_views);
        dt_comments = (TextView) findViewById(R.id.detail_comments);
        dt_comments1 = (TextView) findViewById(R.id.detail_comments1);

        get_content();
    }
    private void get_content()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //jdk, 3.17 16:30,"파이어스토어 연결"
        db.collection("Board")
                .document(BoardID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                boardgetset detail = document.toObject(boardgetset.class);
                                String title = detail.getTitle();
                                String write = detail.getContent();
                                int dviews = detail.getViews();
                                Date write_date = detail.getTime();
                                comments_value = detail.getComments();
                                dt_comments.setText(String.valueOf(comments_value));
                                dt_comments1.setText(String.valueOf(comments_value));
                                dt_title.setText(title);
                                dt_title.setText(title);
                                db.collection("user")
                                        .document(detail.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful())
                                                {
                                                    DocumentSnapshot document = task.getResult();
                                                    User user = document.toObject(User.class);
                                                    dt_uname.setText(user.getNickname());
                                                }
                                            }
                                        });
                                String unencodedHtml = write;
                                String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                                        Base64.NO_PADDING);
                                dt_write.getSettings().setJavaScriptEnabled(true);
                                dt_write.loadData(encodedHtml, "text/html", "base64");
                                dt_views.setText(String.valueOf(dviews));
                                SimpleDateFormat date = new SimpleDateFormat("MM/dd"); // 시간을 출력할 포맷 설정
                                dt_time.setText(date.format(write_date));


                                Log.d("aaaa", "aaaaaaaaaa:" + title  + write + dviews + write_date);
                            } else {
                                Log.d("AAAAA", "No such document");
                            }
                        } else {
                            Log.d("aaaa", "Error getting documents: ", task.getException());
                        }
                    }
                });
        comment_text = (EditText)findViewById(R.id.commenttext);
        getcomments();
    }

    private void add_comment()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long now = System.currentTimeMillis();
        Date date=new Date(now);
        Map<String, Object> comm = new HashMap<>();
        comm.put("uid", user.getUid());
        comm.put("comm", comment_text.getText().toString().trim());
        comm.put("time", date);
        db.collection("Board")
                .document(BoardID)
                .collection("comments")
                .add(comm)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        Map<String, Object> comment = new HashMap<>(); //해쉬맵 선언
                        comment.put("comments", ++comments_value);
                        db.collection("Board")
                                .document(BoardID)
                                .update(comment)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        Log.d("Faberaaaaaaa", "comments value increase  succesfull");
                                    }
                                });
                        Log.d("Faberaaaaaaa", "comm putting succesfull");
                        Toast.makeText(getApplicationContext(), "댓글 작성 완료", Toast.LENGTH_LONG).show();;
                        comment_text.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("faberjooaaaaaaa", "Error putting documents: ");
                    }
                });
        get_content();
    }
    private void getcomments()
    {
        commentslist.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        commentss = (RecyclerView)findViewById(R.id.commentss);
        commentss.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        commentss.setLayoutManager(layoutManager);
        db.collection("Board")
                .document(BoardID)
                .collection("comments")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                comments = document.toObject(commentsgetset.class);
                                Log.d("FABERJOO", String.valueOf(document.getId()));
                                commentslist.add(comments);
                            }
                        } else {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new commentsadpter(commentslist, maincommpage2.this);
                        commentss.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }
}
