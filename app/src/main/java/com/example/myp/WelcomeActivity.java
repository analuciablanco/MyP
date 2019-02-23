package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout joinClassroom = findViewById(R.id.joinClassroomButton);
        joinClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinClassroom = new Intent(WelcomeActivity.this, JoinClassroomActivity.class);
                startActivity(joinClassroom);
            }
        });

        LinearLayout createClassroom = (LinearLayout)findViewById(R.id.createClassroomButton);
        createClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createClassroom = new Intent(WelcomeActivity.this, CreateClassroomActivity.class);
                startActivity(createClassroom);
            }
        });
    }
}