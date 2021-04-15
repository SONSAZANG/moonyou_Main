package com.example.moonyou_test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Date;

public class maincommpage2 extends AppCompatActivity{
    //객체 선언

    Uri video;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincommpage2);

        TextView dt_title = (TextView) findViewById(R.id.detail_title);
        TextView dt_uname = (TextView) findViewById(R.id.detail_username);
        TextView dt_time = (TextView) findViewById(R.id.detail_time);
        WebView dt_write =  findViewById(R.id.webView);
        TextView dt_views = (TextView) findViewById(R.id.detail_views);

        Intent intent = getIntent();
        String BoardID = intent.getStringExtra("BoardID");

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
                                String uname = detail.getUsername();
                                String write = detail.getContent();
                                int dviews = detail.getViews();
                                Date time = detail.getTime();
                                dt_title.setText(title);
                                dt_title.setText(title);
                                dt_uname.setText(uname);
                                String unencodedHtml = write;
                                String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                                        Base64.NO_PADDING);
                                dt_write.getSettings().setJavaScriptEnabled(true);
                                dt_write.loadData(encodedHtml, "text/html", "base64");
                                dt_views.setText(String.valueOf(dviews));
                                dt_time.setText(String.valueOf(time));

                                Log.d("aaaa", "aaaaaaaaaa:" + title + uname + write + dviews + time);
                            } else {
                                Log.d("AAAAA", "No such document");
                            }
                        } else {
                            Log.d("aaaa", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
