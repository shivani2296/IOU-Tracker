package com.nitc.iou_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageGroup extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);
        Button btCreateGroup,btDeleteGroup , btBack;
        btCreateGroup = findViewById(R.id.button22);
        btDeleteGroup = findViewById(R.id.button23);
        btBack = findViewById(R.id.button24);

        btCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageGroup.this, CreateGroupActivity.class));
                finish();
            }
        });
        btDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Activity","Delete_Group");
                Intent toSelectGroup = new Intent(ManageGroup.this,SelectGroupActivity.class);
                toSelectGroup.putExtras(bundle);
                startActivity(toSelectGroup);
                finish();
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageGroup.this, HomeActivity.class));
            }
        });

    }
}
