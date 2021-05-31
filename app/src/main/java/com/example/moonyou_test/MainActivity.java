package com.example.moonyou_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity<fAuth> extends AppCompatActivity {
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    static ArrayList<String> image_Path = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //랭킹포스터 스피너
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //jdk, 3.17 16:30, "DB 연결"
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
        Date time = new Date();
        String date = format.format(time);
        image_Path.clear();
        db.collection("show_info")
                //jdk, 3.17 16:30,"show_info 컬렉션 지정"
                .orderBy("hit", Direction.DESCENDING)
                //jdk, 3.17 16:30,"hit로 내림차순 정렬"
                //.limit(5)//jdk, 3.17 16:30,"5개까지만"
                .get() //jdk, 3.17 16:30,"db 불러오기"
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() //jdk, 3.17 16:30,"작업 완료시"
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                show_info hit = document.toObject(show_info.class); //jdk, 3.17 16:30,"불러운 데이터 document를 show_info 클래스 형식으로 hit 이름으로 저장"
                                show_info hitrank;
                                if(date.compareTo(hit.getFinishday()) <= 0)
                                {
                                    hitrank = document.toObject(show_info.class);
                                    String comm = hitrank.getImage_Path(); //jdk, 3.17 16:30,"hit에서 이미지 경로 받아 comm에 저장"
                                    image_Path.add(comm); //jdk, 3.17 16:30,"arraylist image_Path에 인덱스 추가"
                                    Log.d("faberJOOOOOOO", "MMMMMMMMMMMMMMMMMMMMMMMMM");
                                }
                            }
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        Gallery gallery1 = (Gallery) findViewById(R.id.gallery1); //jdk, 3.17 16:30,"갤러리 뷰 선언 및 연결"
                        MyGalleryAdapter galAdapter = new MyGalleryAdapter(MainActivity.this); //jdk, 3.17 16:30," 액티비티 데이터를 갤러리 어댑터에 넘기고 어댑터 선언"
                        gallery1.setAdapter(galAdapter); //jdk, 3.17 16:30,"갤러리 뷰에 어댑터 지정"
                    }
                });

        Button button1 = (Button) findViewById(R.id.show);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivityForResult(intent, 1);
            }
        });

        Button button2 = (Button) findViewById(R.id.mypage_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mypage_main.class);
                startActivityForResult(intent, 1);
            }
        });

        Button button3 = (Button) findViewById(R.id.menu);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), maincommunity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button button4 = (Button) findViewById(R.id.center);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), help_center.class);
                startActivityForResult(intent, 1);
            }
        });
        Button button5 = (Button) findViewById(R.id.logout);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });



        v_fllipper = findViewById(R.id.image_slide);

        for(int image : images) {
            fllipperImages(image);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String callback = data.getStringExtra("callback");
            switch (callback)
            {
                case "logout":
                    logout();
                case "mypage":
                    Intent intent = new Intent(getApplicationContext(), mypage_main.class);
                    startActivityForResult(intent, 1);
                case "home":
                    resultCode = 0;

            }
        }
    }



    ViewFlipper v_fllipper;

    int images[] = {
            R.drawable.rr1,
            R.drawable.rr2,
            R.drawable.rr3
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
        FirebaseStorage storage = FirebaseStorage.getInstance(); //jdk, 3.17 16:30,"저장소 연결"

        public MyGalleryAdapter(Context c){
            context = c;
        } //jdk, 3.17 16:30,"객체 생성시 액티비티 저장"

        @Override
        public int getCount() {
            return image_Path.size(); //jdk, 3.17 16:30,"데이터의 수(getView를 실행할 횟 수)"
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
            Log.d("faberJOOOOOOO", "VVVVVVVVVVVVVVVVVVVVVVV");
            StorageReference storageref = storage.getReference(); //jdk, 3.17 16:30,"저장소 경로 불러오기"
            StorageReference pathReference = storageref.child(image_Path.get(position)); //jdk, 3.17 16:30,"position(n번째 이미지뷰) 별 저장소 경로 설정"
            ImageView imageview2 = new ImageView(context); //jdk, 3.17 16:30,"이미지 뷰 선언"
            imageview2.setLayoutParams(new Gallery.LayoutParams(400, 500)); //jdk, 3.17 16:30,"이미지 뷰 크기 설정"
            imageview2.setScaleType(ImageView.ScaleType.FIT_CENTER); //jdk, 3.17 16:30,"비율 설정"
            imageview2.setPadding(5,5,5,5); //jdk, 3.17 16:30,"이미지 뷰 여백 설정"



            Glide.with(context).load(pathReference).into(imageview2); //jdk, 3.17 16:30,"파이어베이스 이미지 로더를 이용해 해당 경로의 이미지를 이미지 뷰에 설정"

            return imageview2; //jdk, 3.17 16:30,"이미지 뷰 반환"
        }
    }
    public void logout() {
        fAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    } //로그아웃 기능 구현
}

