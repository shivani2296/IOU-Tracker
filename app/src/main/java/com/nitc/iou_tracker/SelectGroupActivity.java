package com.nitc.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupActivity extends AppCompatActivity {


    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> groupList = new ArrayList();
    String []names;
    String activity;
    Button btBack;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        listView = findViewById(R.id.listG);
        listView.setChoiceMode(ListView.FOCUSABLES_TOUCH_MODE);
        btBack = findViewById(R.id.button17);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            activity = bundle.getString("Activity");

        String email="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
             email = user.getEmail();
        }


        db.collection("Users").whereEqualTo("Email",email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null)
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                    userName = (String) snapshot.get("FName");
                }

                if(userName != null)
                db.collection("Group").whereArrayContains("names",userName)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent( QuerySnapshot queryDocumentSnapshots,  FirebaseFirestoreException e) {
                        groupList.clear();
                        if(queryDocumentSnapshots != null)
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                            groupList.add(snapshot.getString("gName"));
                        }
                        names = new String[groupList.size()];
                        for(int j =0;j <groupList.size();j++){
                            names[j] = groupList.get(j);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_selectable_list_item,groupList);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                });
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("gName",names[position]);


                Intent toSplitBill = new Intent(SelectGroupActivity.this,SplitBillActivity.class);
                toSplitBill.putExtras(bundle);
                Intent toPaidTo = new Intent(SelectGroupActivity.this,paidTo.class);
                toPaidTo.putExtras(bundle);
                Intent toCheckBalance = new Intent(SelectGroupActivity.this,checkBalance.class);
                toCheckBalance.putExtras(bundle);
                Intent toDeleteGroup = new Intent(SelectGroupActivity.this, Delete_Group.class);
                toDeleteGroup.putExtras(bundle);


                if(activity != null && activity.compareTo("Delete_Group") == 0){
                    startActivity(toDeleteGroup);
                    finish();
                }else if(activity != null && activity.compareTo("paidTo") == 0){
                    startActivity(toPaidTo);
                    finish();
                }
                else if(activity != null && activity.compareTo("checkBalance") == 0){
                    startActivity(toCheckBalance);
                    finish();
                }
                else {
                    startActivity(toSplitBill);
                    finish();
                }
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectGroupActivity.this,HomeActivity.class));
            }
        });

    }
}
