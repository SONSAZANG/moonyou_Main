package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class book_final extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_final);



        TextView tx1 = (TextView) findViewById(R.id.seat_text);
        TextView tx2 = (TextView) findViewById(R.id.date_text);

        Intent select1 = getIntent();
        String select = select1.getStringExtra("select");
        String date = select1.getStringExtra("date");
        tx1.setText(select);
        tx2.setText(date);


    }


}
