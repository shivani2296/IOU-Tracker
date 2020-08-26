package com.nitc.iou_tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SplitBillActivity extends AppCompatActivity {

    ArrayList<String> selectedItems = new ArrayList<>();
    ListView listView;
    Button btSplit,btBack;
    Spinner spinner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> nameList = new ArrayList();
    EditText amount;
    double amount2;
    double amount1;
    String gName;
    int flag=0;
    String paidBy;
    CollectionReference billRef = db.collection("Bill");
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            gName = bundle.getString("gName");

        spinner =  findViewById(R.id.spinner);
        listView = findViewById(R.id.listPerson);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        amount = findViewById(R.id.editText7);
        btSplit = findViewById(R.id.button12);
        btBack = findViewById(R.id.button16);
        progressBar = findViewById(R.id.progressBar5);

        db.collection("Group").whereEqualTo("gName",gName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots,  FirebaseFirestoreException e) {
                nameList.clear();
                if(queryDocumentSnapshots != null)
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                    nameList= (ArrayList<String>)snapshot.get("names");
                }
                String[] names = new String[nameList.size()];
                for(int j =0;j<nameList.size();j++){
                    names[j] = nameList.get(j);
                }
                Arrays.sort(names);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SplitBillActivity.this,R.layout.row_layout, R.id.txt_lan,names);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem =((TextView)view).getText().toString();
                        if(selectedItems.contains(selectedItem)){
                            selectedItems.remove(selectedItem);
                        }
                        else
                            selectedItems.add(selectedItem);
                    }
                });
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SplitBillActivity.this,android.R.layout.simple_spinner_item,names);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter1);
            }
        });



        btSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = amount.getText().toString();
                if(s.trim().isEmpty()){
                    amount.setError("Please enter Amount");
                    amount.requestFocus();
                }
                else {
                    amount1 = Double.parseDouble(s);
                    paidBy = (String)spinner.getSelectedItem();
                    splitBill();
                }
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplitBillActivity.this,HomeActivity.class));
            }
        });

    }


    public void splitBill(){
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String , Double> map = new HashMap<>();
        for(int i =0;i<selectedItems.size();i++){
            map.put(selectedItems.get(i),amount1/selectedItems.size());
        }

        if(map.get(paidBy) != null){
            map.replace(paidBy,(amount1/selectedItems.size()) - amount1);
        }
        else{
            map.put(paidBy, -amount1);
        }
        db.collection("Bill").whereEqualTo("billNo",gName)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @SuppressLint("NewApi")
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if ( queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty() && flag ==0) {
                        flag = 1;
                        HashMap<String, Double> map1 = new HashMap<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            map1 = (HashMap<String, Double>) snapshot.get("listOfPerson");
                           amount2 = (Double)snapshot.get("amount");
                        }
                        Set<String> key = new HashSet<>();

                        key = map.keySet();
                        Double d1;
                        for (String ele : key) {
                            Double d = map1.get(ele);
                            if (ele == paidBy) {

                                if (d != null) {
                                    Double d2 = map.get(ele);
                                    d1 =  d + d2;
                                    map1.replace(ele, d1);
                                } else {
                                    d1 =   map.get(ele);
                                    map1.put(ele, d1);
                                }
                            }
                            else{
                                if (d != null) {
                                    d1 = map.get(ele) + d;
                                    map1.replace(ele, d1);
                                } else {
                                    d1 = map.get(ele);
                                    map1.put(ele, d1);
                                }
                            }
                        }

                        Bill bill  = new Bill(gName, map1, amount1 + amount2);
                        billRef.document(gName).set(bill);
                        Toast.makeText(SplitBillActivity.this,"Bill Split Done", Toast.LENGTH_LONG).show();
                        Intent toBack = new Intent(SplitBillActivity.this, HomeActivity.class);
                        startActivity(toBack);
                        finish();

                    } else if(flag == 0){
                        Bill bill  = new Bill(gName, map, amount1);
                        billRef.document(gName).set(bill);
                        Toast.makeText(SplitBillActivity.this,"Bill Split Done", Toast.LENGTH_LONG).show();

                        flag =1;
                        Intent toBack = new Intent(SplitBillActivity.this, HomeActivity.class);
                        startActivity(toBack);
                        finish();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
    }
}
