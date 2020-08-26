package com.nitc.iou_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HaveGroupActivity extends AppCompatActivity {

    Button btBack,btCreateGroup,btSelectGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_group);
        btBack = findViewById(R.id.button10);
        btCreateGroup = findViewById(R.id.button9);
        btSelectGroup = findViewById(R.id.button8);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHome = new Intent(HaveGroupActivity.this,HomeActivity.class);
                startActivity(toHome);
                finish();
            }
        });

        btCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCreateGroup = new Intent(HaveGroupActivity.this, CreateGroupActivity.class);
                startActivity(toCreateGroup);
                finish();
            }
        });

        btSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSelectGroup = new Intent(HaveGroupActivity.this,SelectGroupActivity.class);
                startActivity(toSelectGroup);
                finish();
            }
        });
    }
}
