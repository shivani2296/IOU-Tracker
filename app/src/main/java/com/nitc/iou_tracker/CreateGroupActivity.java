package com.nitc.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {


    ArrayList<String> selectedItems = new ArrayList<>();
    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btShowName,btBack;
    EditText gName;
    CollectionReference groupRef = db.collection("Group");
    String userName = "",allReadyGroup=" ";
    int flag = 0;
    List<String> nameList = new ArrayList();
    private View view;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        gName = findViewById(R.id.editText5);
        btShowName = findViewById(R.id.button11);
        listView = findViewById(R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        btBack = findViewById(R.id.button19);
        progressBar = findViewById(R.id.progressBar4);


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

                        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent( QuerySnapshot queryDocumentSnapshots,  FirebaseFirestoreException e) {
                                nameList.clear();
                                if(queryDocumentSnapshots != null )
                                if(!queryDocumentSnapshots.isEmpty())
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                                    nameList.add(snapshot.getString("FName"));
                                }
                                String names[] = new String[nameList.size()];
                                for(int j =0;j<nameList.size();j++){
                                    names[j] = nameList.get(j);
                                }
                                Arrays.sort(names);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateGroupActivity.this,R.layout.row_layout, R.id.txt_lan,names);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String selectedItem =((TextView)view).getText().toString();
                                        if(selectedItems.contains(selectedItem)){
                                            selectedItems.remove(selectedItem);
                                        }
                                        else {
                                            selectedItems.add(selectedItem);

                                        }
                                    }
                                });
                            }
                        });
                    }
                });

        btShowName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedItems.isEmpty()){
                    Toast.makeText(CreateGroupActivity.this,"Please select Group member",Toast.LENGTH_LONG).show();
                    listView.requestFocus();
                }else {
                    flag = 0;
                    addGroup();
                }
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGroupActivity.this,HomeActivity.class));
            }
        });

    }
    public void addGroup(){
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Group").whereEqualTo("gName",gName.getText().toString())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                                allReadyGroup = (String)snapshot.get("gName");
                            }
                        }
                        if(gName.getText().toString().compareTo(allReadyGroup)==0 && flag ==0) {
                            Toast.makeText(CreateGroupActivity.this,"Group Name already Exist",Toast.LENGTH_LONG).show();
                            gName.setError("Please enter new Group Name");
                            gName.requestFocus();
                            flag = 1;
                        }else if(gName.getText().toString().trim().isEmpty()){
                            gName.setError("Please enter Group Name");
                            gName.requestFocus();
                        } else if(flag == 0){
                            flag=1;
                            Group group = new Group(gName.getText().toString(), selectedItems.size(), selectedItems ,userName);
                            groupRef.document(gName.getText().toString()).set(group);
                            Toast.makeText(CreateGroupActivity.this,"New Group Created",Toast.LENGTH_LONG).show();
                            Intent toBack = new Intent(CreateGroupActivity.this, HomeActivity.class);
                            startActivity(toBack);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

}
