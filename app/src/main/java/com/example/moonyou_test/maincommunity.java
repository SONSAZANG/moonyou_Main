package com.example.moonyou_test;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class maincommunity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincommunity);
        v_fllipper = findViewById(R.id.image_slide2);
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
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
    }
