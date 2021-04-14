package com.example.moonyou_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class book_final extends AppCompatActivity {

    StorageReference storageref;
    FirebaseStorage storage;
    FirebaseFirestore db;
    show_info show_info;
    String showID;
    TextView tx1;
    TextView tx2;
    TextView tx3;
    ImageView ig1;
    Button btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_final);

        Intent select1 = getIntent();
        String select = select1.getStringExtra("select");
        String date = select1.getStringExtra("date");
        showID = select1.getStringExtra("show_id");
        String time = select1.getStringExtra("time");
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();
        tx1 = findViewById(R.id.BookFinalName);
        tx2 = findViewById(R.id.BookFinalSeat);
        tx3 = findViewById(R.id.BookFinalTime);
        ig1 = findViewById(R.id.BookFinalImage);
        btn1 = findViewById(R.id.BookFinalCancel);
        btn2 = findViewById(R.id.BookFinalOK);
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
                            tx1.setText(show_info.getTitle());
                            StorageReference pathReference = storageref.child(show_info.getImage_Path()); //jdk, 3.17 16:30,"position(n번째 이미지뷰) 별 저장소 경로 설정"
                            Glide.with(book_final.this).load(pathReference).into(ig1);
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }

                    }
                });



        // date 값 substring 사용해서 문자열 형식 변경
        String day = date.substring(0,2);
        String month = date.substring(3,5);
        String year = date.substring(6,8);
        String dateA = "20" + day + "년 " + month + "월 " + year + "일 ";



        String selectA = select.replaceAll("," , "번 ");
        String[] seatA;
        seatA = selectA.split("번 ");
        tx2.setText(selectA);
        tx3.setText(dateA + time);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseUser user= fAuth.getCurrentUser();
                Map<String, Object> resv_info = new HashMap<>();
                resv_info.put("title", show_info.getTitle());
                resv_info.put("date", dateA);
                resv_info.put("time", time);
                resv_info.put("seatCount", seatA.length);
                for(int i = 1; i <= seatA.length; i++)
                {
                    resv_info.put("seat_num" + String.valueOf(i), seatA[i-1] + "번");

                }

                //jdk, 3.17 16:30,"키에 문의내용 저장"
                db.collection("user")
                        .document(user.getUid())
                        .collection("resv_his")//jdk, 3.17 16:30," 컬렉션에"
                        .add(resv_info) //jdk, 3.17 16:30,"임의의 문서ID로 데이터 추가"
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) { //jdk, 3.17 16:30,"성공시"
                                Log.d("Faber", "Document ID = " );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("faber", "Document Error!!");
                            }
                        });
                Log.d("aaa", "DocumentSnapshot data: ");
            }
        });
    }
}
