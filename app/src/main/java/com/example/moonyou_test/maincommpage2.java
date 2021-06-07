package com.example.moonyou_test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class maincommpage2 extends AppCompatActivity{
    //객체 선언
    EditText comment_text;
    String BoardID;
    int comments;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincommpage2);

        TextView dt_title = (TextView) findViewById(R.id.detail_title);
        TextView dt_uname = (TextView) findViewById(R.id.detail_username);
        TextView dt_time = (TextView) findViewById(R.id.detail_time);
        WebView dt_write =  findViewById(R.id.webView);
        TextView dt_views = (TextView) findViewById(R.id.detail_views);

        Intent intent = getIntent();
        BoardID = intent.getStringExtra("BoardID");

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
                                Date time = detail.getTime();
                                comments = detail.getComments();
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
                                dt_time.setText(String.valueOf(time));

                                Log.d("aaaa", "aaaaaaaaaa:" + title  + write + dviews + time);
                            } else {
                                Log.d("AAAAA", "No such document");
                            }
                        } else {
                            Log.d("aaaa", "Error getting documents: ", task.getException());
                        }
                    }
                });
        comment_text = (EditText)findViewById(R.id.commenttext);
        Button create_comment = (Button)findViewById(R.id.create_comment);
        create_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment_text.getText().toString().trim().equals(""))
                {
                    add_comment();
                }
            }
        });
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
                        comment.put("comments", ++comments);
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
    }
}
