package com.example.moonyou_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mypage_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_main);

        Button button3 = (Button) findViewById(R.id.myticket_btn);
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),mypage_ticket.class);
                startActivity(intent);
            }
        });

        Button button4 = (Button) findViewById(R.id.myreview_btn);
        button4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),mypage_review.class);
                startActivity(intent);
            }
        });
    }
}