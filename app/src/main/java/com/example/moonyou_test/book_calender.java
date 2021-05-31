package com.example.moonyou_test;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class book_calender extends AppCompatActivity implements timeadpter.OnListItemSelectedInterface {

    TextView title_label;
    TextView selectDate;
    show_info show_info;
    StorageReference storageref;
    FirebaseStorage storage;
    String strDate;
    String finDate;
    String date;
    ArrayList<timegetset> timelist = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    String showID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_calender);
        Intent intent1 = getIntent();
        showID = intent1.getStringExtra("show_id"); // id가져오기
        Toast.makeText(getApplicationContext(), showID, Toast.LENGTH_SHORT).show();
        title_label = findViewById(R.id.title1);
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();

        CalendarView calendarView = (CalendarView) findViewById(R.id.simple_calendarview);
        db = FirebaseFirestore.getInstance();
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
                            Log.d("sonsazang", show_info.getStartday());
                            Date time = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd");
                            String datee = dateFormat.format(time);
                            strDate = show_info.getStartday();
                            finDate = show_info.getFinishday();
                            Date strDay;
                            if(datee.compareTo(strDate) <= 0){
                                strDay = dateFormat.parse(strDate, new ParsePosition(0));
                            }
                            else
                            {
                                strDay = dateFormat.parse(datee, new ParsePosition(0));
                            }
                            Date finDay = dateFormat.parse(finDate, new ParsePosition(0));
                            Long strLong = strDay.getTime();
                            Long finLong = finDay.getTime();
                            calendarView.setMaxDate(finLong);
                            calendarView.setMinDate(strLong);
                            Date initialdate = new Date(calendarView.getDate());
                            date = dateFormat.format(initialdate);
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        showtimetable();
                    }
                });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String mon;
                if ((month + 1) < 10)
                {
                    mon = "0" + (month + 1);
                }
                else
                {
                    mon = String.valueOf((month + 1));
                }
                String day;
                if ((dayOfMonth) < 10)
                {
                    day = "0" + dayOfMonth;
                }
                else
                {
                    day = String.valueOf(dayOfMonth);
                }
                date = (year - 2000) + "." + mon + "." + day;
                showtimetable();
            }
        });
        Button button2 = (Button) findViewById(R.id.home);
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
                outIntent.putExtra("callback", "home");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        Button button4 = (Button) findViewById(R.id.mypage_btn);
        button4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
                outIntent.putExtra("callback", "mypage");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
        Button button5 = (Button) findViewById(R.id.logout);
        button5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), MainActivity2.class);
                outIntent.putExtra("callback", "logout");
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }

    public void showtimetable()
    {
        timelist.clear();
        db.clearPersistence();
        recyclerView = findViewById(R.id.timetable);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(book_calender.this);
        recyclerView.setLayoutManager(layoutManager);
        db.collection("show_info")
                .document(showID)
                .collection("schedule")
                .document(date)
                .collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            {
                                timegetset time = document.toObject(timegetset.class);
                                time.setTime(document.getId());
                                time.setShow_id(showID);
                                int ls = 0, ts = 0;
                                String[] seat = time.seat_array.split("");
                                for (String s : seat)
                                {
                                    if(!s.equals("_"))
                                    {
                                        if (!s.equals("/"))
                                        {
                                            ts++;
                                            time.setTotal_seat(ts);
                                            if (s.equals("A"))
                                            {
                                                ls++;
                                                time.setLeft_seat(ls);
                                            }
                                        }
                                    }
                                }
                                time.setDate(date);
                                timelist.add(time);
                            }
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        adapter = new timeadpter(timelist, book_calender.this, book_calender.this::onListItemSelected);
                        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                    }
                });
    }

    public void onListItemSelected(View v, int pos) {
        timeadpter.itemViewHolder viewHolder = (timeadpter.itemViewHolder)recyclerView.findViewHolderForAdapterPosition(pos);
        timegetset item = timelist.get(pos) ;
        Button Resv = (Button)findViewById(R.id.resv);
        Resv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (pos != RecyclerView.NO_POSITION)
                {
                    // 데이터 리스트로부터 아이템 데이터 참조
                    timegetset item = timelist.get(pos);
                    Toast.makeText(v.getContext(), item.getTime(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), book_seat.class);
                    intent.putExtra("time", item.getTime());
                    intent.putExtra("date", item.getDate());
                    intent.putExtra("time", item.getTime());
                    intent.putExtra("show_id", item.getShow_id());
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String callback = data.getStringExtra("callback");
            Intent outIntent = new Intent(getApplicationContext(), show_main.class);
            outIntent.putExtra("callback", callback);
            setResult(RESULT_OK, outIntent);
            finish();
        }
    }
}
