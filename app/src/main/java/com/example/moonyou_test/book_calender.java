package com.example.moonyou_test;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
        import androidx.annotation.IdRes;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.OrientationHelper;
        import androidx.appcompat.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
        import android.widget.Toast;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.List;


public class book_calender extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_calender);

        Button button = (Button) findViewById(R.id.seatchoice_btn);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),book_seat.class);
                startActivity(intent);
            }
        });
    }
}
