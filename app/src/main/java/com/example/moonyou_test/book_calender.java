package com.example.moonyou_test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.w3c.dom.Text;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


public class book_calender extends AppCompatActivity {

    TextView title_label;
    TextView start;
    TextView finish;
    TextView selectDate;
    show_info show_info;
    StorageReference storageref;
    FirebaseStorage storage;
    String strDate;
    String finDate;
    String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_calender);

        Intent intent1 = getIntent();
        String showID = intent1.getStringExtra("show_id"); // id가져오기
        Toast.makeText(getApplicationContext(), showID, Toast.LENGTH_SHORT).show();

        start = findViewById(R.id.startDay);
        finish = findViewById(R.id.finishDay);
        title_label = findViewById(R.id.title1);
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();

        CalendarView calendarView = (CalendarView) findViewById(R.id.simple_calendarview);

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
                             //jdk, 3.17 16:30,"position(n번째 이미지뷰) 별 저장소 경로 설정"
                            title_label.setText(show_info.getTitle());
                            start.setText(show_info.getStartday());
                            finish.setText(show_info.getFinishday());
                            Log.d("sonsazang", show_info.getStartday());

                            strDate = show_info.getStartday();
                            finDate = show_info.getFinishday();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd");
                            Date strDay = dateFormat.parse(strDate, new ParsePosition(0));
                            Date finDay = dateFormat.parse(finDate, new ParsePosition(0));
                            Long strLong = strDay.getTime();
                            Long finLong = finDay.getTime();
                            calendarView.setMaxDate(finLong);
                            calendarView.setMinDate(strLong);


                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                    }
                });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "/" + (month + 1) + "/" + dayOfMonth;
                Button button1 = (Button) findViewById(R.id.dateconfirm_btn);
                button1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(book_calender.this, book_seat.class);
                        intent.putExtra("date", date);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
