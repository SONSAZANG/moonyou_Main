package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;

public class myticket extends AppCompatActivity {

    StorageReference storageref;
    FirebaseStorage storage;
    FirebaseFirestore db;
    mypage_getset book_info;
    String showID;
    TextView tx1;
    TextView tx2;
    TextView tx3;
    TextView tx4;
    ImageView ig1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myticket);

        Intent select1 = getIntent();
        String book_ID = select1.getStringExtra("book_ID");
        showID = select1.getStringExtra("show_ID");
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user= fAuth.getCurrentUser();
        tx1 = findViewById(R.id.TicketID);
        tx2 = findViewById(R.id.TicketName);
        tx3 = findViewById(R.id.TicketSeat);
        tx4 = findViewById(R.id.TicketTime);
        ig1 = findViewById(R.id.TicketImage);
        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .document(user.getUid())
                .collection("resv_his")
                .document(book_ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) //jdk, 3.17 16:30,"성공 했을 시"
                        {
                            DocumentSnapshot document = task.getResult(); //jdk, 3.17 16:30,"불러온 데이터 전체를 document에 하나씩 넣어서"
                            book_info = document.toObject(mypage_getset.class);
                        }
                        else
                        {
                            Log.d("faberJOOOOOOO", "Error : ", task.getException());
                        }
                        StorageReference pathReference = storageref.child(book_info.getImage());
                        Glide.with(myticket.this).load(pathReference).into(ig1);
                        tx1.setText(book_ID);
                        tx2.setText(book_info.getTitle());
                        tx3.setText(book_info.getSeatName());
                        tx4.setText(book_info.getDate() + book_info.getTime());
                    }
                });

    }
}
