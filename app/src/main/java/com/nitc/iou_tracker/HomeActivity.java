package com.nitc.iou_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout,btBillSplit,btManageGroup,btCheckBalance,btPaidTo,btUpdateProfile;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLogout = findViewById(R.id.button2);
        btBillSplit = findViewById(R.id.button3);
        btManageGroup = findViewById(R.id.button4);
        btPaidTo = findViewById(R.id.button6);
        btCheckBalance = findViewById(R.id.button5);
        btUpdateProfile = findViewById(R.id.button7);
        progressBar = findViewById(R.id.progressBar6);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intToMain = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intToMain);
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });

        btBillSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToSpilt = new Intent(HomeActivity.this, HaveGroupActivity.class);
                startActivity(ToSpilt);
                finish();
            }
        });

        btManageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToCreateGroup = new Intent(HomeActivity.this,ManageGroup.class);
                startActivity(ToCreateGroup);
                finish();
            }
        });
        btPaidTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSelecetGroup = new Intent(HomeActivity.this,SelectGroupActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("Activity","paidTo");
                toSelecetGroup.putExtras(bundle);
                startActivity(toSelecetGroup);
                finish();
            }
        });
        btCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSelectGroup = new Intent(HomeActivity.this,SelectGroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Activity","checkBalance");
                toSelectGroup.putExtras(bundle);
                startActivity(toSelectGroup);
                finish();
            }
        });
        btUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUpdate = new Intent(HomeActivity.this,Update_Profile.class);
                startActivity(toUpdate);
                finish();
            }
        });
    }
}
