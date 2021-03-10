package com.example.moonyou_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity<fAuth> extends AppCompatActivity {
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //랭킹포스터 스피너
        Gallery gallery1 = (Gallery) findViewById(R.id.gallery1);
        MyGalleryAdapter galAdapter = new MyGalleryAdapter(this);
        gallery1.setAdapter(galAdapter);

        Button button1 = (Button) findViewById(R.id.show);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.mypage_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mypage_main.class);
                startActivity(intent);
            }
        });

        Button button3 = (Button) findViewById(R.id.menu);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), maincommunity.class);
                startActivity(intent);
            }
        });

        Button button4 = (Button) findViewById(R.id.center);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), help_center.class);
                startActivity(intent);
            }
        });

        v_fllipper = findViewById(R.id.image_slide);

        for(int image : images) {
            fllipperImages(image);
        }
    }



    ViewFlipper v_fllipper;

    int images[] = {
            R.drawable.top_image01,
            R.drawable.top_image02,
            R.drawable.top_image03
    };


    // 이미지 슬라이더 구현 메서드
    public void fllipperImages(int image) {
        ImageView imageView1 = new ImageView(this);
        imageView1.setBackgroundResource(image);

        v_fllipper.addView(imageView1);      // 이미지 추가
        v_fllipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }

    public class MyGalleryAdapter extends BaseAdapter{
        Context context;
        Integer[] posterID = {R.drawable.rankposter1, R.drawable.rankposter2, R.drawable.rankposter3
                ,R.drawable.rankposter4,R.drawable.rankposter5};

        public MyGalleryAdapter(Context c){
            context = c;
        }

        @Override
        public int getCount() {
            return posterID.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageview2 = new ImageView(context);
            imageview2.setLayoutParams(new Gallery.LayoutParams(400, 500));
            imageview2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageview2.setPadding(5,5,5,5);

            imageview2.setImageResource(posterID[position]);

            return imageview2;
        }
    }
    public void logout(View view) {
        fAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    } //로그아웃 기능 구현
}

