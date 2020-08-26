package com.nitc.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reminder extends AppCompatActivity {

    //Declaring EditText
    private EditText editTextEmail;
    private EditText editTextMessage;
    private ProgressBar progressBar;
    Button btBack;
    String userId;
    private FirebaseFirestore db ;
    private String uName,gName;
    private  String subject;
    private ArrayList<String> users=new ArrayList<>();
    private Spinner spinner;
    private String email ="";
    private int flag = 0;

    //Send button
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        progressBar = findViewById(R.id.progressBar2);

        spinner = findViewById(R.id.spinner3);
        btBack = findViewById(R.id.button18);
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            gName = bundle.getString("gName");
        }
        //Adding click listener


        db.collection("Bill").whereEqualTo("billNo",gName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        HashMap<String , Double> map = new HashMap<>();
                        if(queryDocumentSnapshots != null) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                map = (HashMap<String, Double>) snapshot.get("listOfPerson");
                            }
                            if (map != null) {

                                for (Map.Entry<String, Double> entry : map.entrySet()) {
                                    if (entry.getValue() > 0) {
                                        users.add(entry.getKey());
                                    }
                                }
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Reminder.this, android.R.layout.simple_spinner_item, users);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter1);

                            }
                        }
                    }
                });



        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }

        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Reminder.this,HomeActivity.class));
            }
        });

    }

    private void sendEmail() {
        //Getting content for email

        subject = "For Refund Money from ";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        String name = (String)spinner.getSelectedItem();
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Users").whereEqualTo("FName",name)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(queryDocumentSnapshots != null){
                            for(DocumentSnapshot snapshots : queryDocumentSnapshots){
                                email = (String)snapshots.get("Email");
                            }
                            db.collection("Users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    if(documentSnapshot != null){
                                        uName =(String)documentSnapshot.get("FName");
                                        subject += uName;
                                    }
                                    String message = editTextMessage.getText().toString().trim();
                                    if(subject.isEmpty()){
                                        editTextMessage.setError("Please Enter Message");
                                        editTextMessage.requestFocus();
                                    }else if(flag == 0){
                                        flag = 1;
                                        //Creating SendMail object
                                        SendMail sm = new SendMail(Reminder.this, email, subject, message);

                                        //Executing sendmail to send email
                                        sm.execute();
                                        startActivity(new Intent(Reminder.this,HomeActivity.class));
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        }
                    }
                });

    }

//    @Override
//    public void onClick(View v) {
//        sendEmail();
//    }
}

