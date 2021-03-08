package com.example.moonyou_test;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import android.os.Bundle;
=======
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
>>>>>>> c3fca53c0029a91d7d21f374cfaf44a4857e93ff

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_before);
<<<<<<< HEAD
=======

        Button button1 = (Button) findViewById(R.id.showmain_btn);
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),show_main.class);
                startActivity(intent);
            }
        });
>>>>>>> c3fca53c0029a91d7d21f374cfaf44a4857e93ff
    }
}