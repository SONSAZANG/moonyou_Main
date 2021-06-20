package com.example.moonyou_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lumyjuwon.richwysiwygeditor.RichWysiwyg;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class board_write extends AppCompatActivity {

    private RichWysiwyg wysiwyg;
    String Boardtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        Intent intent1 = getIntent();
        Boardtype = intent1.getStringExtra("Boardtype");
        wysiwyg = findViewById(R.id.richwysiwygeditor);
        wysiwyg.getContent()
                .setEditorFontSize(18)
                .setEditorPadding(4, 0, 4, 0);

        wysiwyg.getHeadlineEditText().setHint("Headline");

        wysiwyg.getCancelButton().setText("취소");
        wysiwyg.getCancelButton().setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), maincommunity.class);
                startActivity(intent);
            }
        });
        wysiwyg.getConfirmButton().setText("확인");
        wysiwyg.getConfirmButton().setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=  wysiwyg.getHeadlineEditText().getText().toString(); //헤드라인
                    String content= wysiwyg.getContent().getHtml(); //텍스트내용
                addData(title,content);//jdk, 3.17 16:30," 저장된 텍스트를 전달하여 db에 저장하는 함수 실행 "
                Intent outIntent = new Intent(getApplicationContext(), maincommunity.class);
                outIntent.putExtra("callback", "writesucess");
                outIntent.putExtra("Boardtype", Boardtype);
                setResult(RESULT_OK, outIntent);
                finish();
            }
            // Handle this
           /*     Log.i("Rich Wysiwyg Headline", wysiwyg.getHeadlineEditText().getText().toString());
                if(wysiwyg.getContent().getHtml() != null)
                    Log.i("Rich Wysiwyg", wysiwyg.getContent().getHtml());
            }*/

        });
    }
    private void addData(String title,String content) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
             FirebaseUser user= fAuth.getCurrentUser();
        String email = user.getEmail();//jdk, 3.17 16:30,"현재 로그인 유저 불러오기"
     db.collection("user")
             .whereEqualTo("email", email)
             .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                         @Override
                                         public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                             if (task.isSuccessful()) {
                                                 for (QueryDocumentSnapshot document : task.getResult()) {
                                                     User name = document.toObject(User.class);
                                                     String naming = name.getNickname();
                                                     long now = System.currentTimeMillis();
                                                     Date date=new Date(now);
                                                     Map<String, Object> comm = new HashMap<>(); //jdk, 3.17 16:30," 해쉬맵 선언"
                                                     comm.put("uid",user.getUid());
                                                     comm.put("title",title);
                                                     comm.put("content",content);
                                                     comm.put("views",0);
                                                     comm.put("like",0);
                                                     comm.put("comments",0);
                                                     comm.put("time",date);
                                                     //jdk, 3.17 16:30,"키에 문의내용 저장"
                                                     db.collection("Board") //jdk, 3.17 16:30," 컬렉션에"
                                                             .add(comm) //jdk, 3.17 16:30,"임의의 문서ID로 데이터 추가"
                                                             .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                 @Override
                                                                 public void onSuccess(DocumentReference documentReference) { //jdk, 3.17 16:30,"성공시"
                                                                     Log.d("Faber", "Document ID = " + comm);
                                                                 }
                                                             }).addOnFailureListener(new OnFailureListener() {
                                                         @Override
                                                         public void onFailure(@NonNull Exception e) {
                                                             Log.d("faber", "Document Error!!");
                                                         }
                                                     });
                                                     Log.d("aaa", "DocumentSnapshot data: " + naming);
                                                 }
                                             } else {
                                                 Log.d("aaaaaaba", "Error getting documents: ", task.getException());
                                             }
                                         }
                                     });
      //시간

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> images = ImagePicker.getImages(data);
            insertImages(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {
            stringBuffer.append(images.get(i).getPath()).append("\n");
            // Handle this
            wysiwyg.getContent().insertImage("file://" + images.get(i).getPath(), "alt");
        }
    }
}
