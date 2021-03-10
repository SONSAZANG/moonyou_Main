package com.example.moonyou_test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialCalendar;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


public class book_calender extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_calender);



        CalendarView calendarView = (CalendarView) findViewById(R.id.simple_calendarview);
        long selectedDate = calendarView.getDate();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String date = year + "/" + month + "/" + dayOfMonth;
                Intent intent = new Intent(book_calender.this, book_seat.class);
                intent.putExtra("date", date);

                Button button1 = (Button) findViewById(R.id.dateconfirm_btn);
                button1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                });

            }
        });






    }
}
