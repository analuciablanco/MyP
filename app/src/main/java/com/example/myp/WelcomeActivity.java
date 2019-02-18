package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity {

    Button buttonCreateClass;
    Button buttonJoinClassroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        LinearLayout joinclassroom = (LinearLayout)findViewById(R.id.CreateClassroom);
        joinclassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinclassroom = new Intent(WelcomeActivity.this, JoinClassroomActivity.class);
                startActivity(joinclassroom);
            }
        });

        LinearLayout createclassroom = (LinearLayout)findViewById(R.id.CreateClassroom);
        createclassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createclassroom = new Intent(WelcomeActivity.this, CreateClassroomActivity.class);
                startActivity(createclassroom);
            }
        });
    }
}