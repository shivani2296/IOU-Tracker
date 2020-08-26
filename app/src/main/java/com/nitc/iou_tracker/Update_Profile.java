package com.nitc.iou_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Update_Profile extends AppCompatActivity {

    private EditText  name, mobile ;
    Button updatebtn,btBack;
    private FirebaseFirestore db ;

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile);


        db = FirebaseFirestore.getInstance();
        name = findViewById(R.id.editText_name);
        mobile = findViewById(R.id.editText_mobile);
        updatebtn = findViewById(R.id.button_update);
        btBack = findViewById(R.id.button);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString().trim();
                String newMobile = mobile.getText().toString().trim();


                if (newName.isEmpty()) {
                    name.setError("Please enter Name");
                    name.requestFocus();
                }
                if (newMobile.isEmpty()) {
                    mobile.setError("Please enter mobile number");
                    mobile.requestFocus();
                }else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    userId = user.getUid();
                    if (user != null) {
                        final DocumentReference documentReference = db.collection("Users").document(userId);
                        Map<String, Object> updatemap = new HashMap<>();
                        updatemap.put("FName", newName);
                        updatemap.put("Contact", newMobile);
                        documentReference.update(updatemap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "OnSuccess: User profile is updated");
                                        Toast.makeText(Update_Profile.this, "your profile is updated ", Toast.LENGTH_SHORT).show();
                                        Intent ToHome = new Intent(Update_Profile.this, HomeActivity.class);
                                        startActivity(ToHome);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("TAG", "OnFailure: ", e);
                                    }
                                });
                        Log.d("OnSuccess", "User profile is updated" + userId);
                        Intent intent = new Intent(Update_Profile.this,HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // No user is signed in
                    }
                }
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_Profile.this, HomeActivity.class));
            }
        });
        }


        //mFirebaseAuth = FirebaseAuth.getInstance();
        //userId = mFirebaseAuth.getCurrentUser().getUid();

}

