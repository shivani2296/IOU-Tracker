package com.nitc.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class checkBalance extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String gName;
    Button btReminder,btBack;
    private int flag= 0;
    private String userId,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        TextView groupName = findViewById(R.id.textView6);
        ListView listView = findViewById(R.id.listBalance);
        btReminder = findViewById(R.id.button15);
        btBack = findViewById(R.id.button20);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            gName = bundle.getString("gName");
            groupName.setText(gName);
        }



        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(checkBalance.this,HomeActivity.class));
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        db.collection("Users").document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot != null){
                            userName = (String)documentSnapshot.get("FName");
                        }
                        db.collection("Bill").whereEqualTo("billNo",gName).
                                addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        HashMap<String, Double> map = new HashMap<>();
                                        if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                map = (HashMap<String, Double>) snapshot.get("listOfPerson");
                                            }


                                            ArrayList<String> balance = new ArrayList<>();
                                            if (map != null) {
                                                for (Map.Entry<String, Double> entry : map.entrySet()) {
                                                    if(entry.getKey().compareTo(userName) ==  0){
                                                        if(entry.getValue() >= 0 ){
                                                            flag = 1;
                                                        }
                                                    }
                                                    balance.add(entry.getKey() + ":     " + entry.getValue());
                                                }
                                            } else {
                                                balance.add("No Bill Split yet.");
                                            }

                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, balance);
                                            adapter.notifyDataSetChanged();
                                            listView.setAdapter(adapter);
                                        }

                                    }
                                });
                    }
                });

        btReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 1){
                    Toast.makeText(checkBalance.this,"you cannot send reminder",Toast.LENGTH_LONG).show();

                }else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("gName", gName);
                    Intent toReminder = new Intent(checkBalance.this, Reminder.class);
                    toReminder.putExtras(bundle1);
                    startActivity(toReminder);
                    finish();
                }
            }
        });
    }
}
