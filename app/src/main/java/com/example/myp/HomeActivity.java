package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

// Pantalla principal tras iniciar sesión
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Function to delete the top back arrow on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Button to Create Classrooms
        final LinearLayout createClassroom = findViewById(R.id.createClassroomButton);
        createClassroom.setClickable(true);
        createClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClassroom.setClickable(false);
                Intent createClassroom = new Intent(HomeActivity.this, CreateClassroomActivity.class);
                startActivity(createClassroom);
            }
        });

        // Button to Join Classrooms
        final LinearLayout joinClassroom = findViewById(R.id.joinClassroomButton);
        joinClassroom.setClickable(true);
        joinClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClassroom.setClickable(false);
                Intent joinClassroom = new Intent(HomeActivity.this, JoinClassroomActivity.class);
                startActivity(joinClassroom);
            }
        });

        // Button to Navigate to your classrooms
        final LinearLayout backToChatList = findViewById(R.id.backChatListButton);
        backToChatList.setClickable(true);
        backToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToChatList.setClickable(false);
                Intent ClassroomActivity = new Intent(HomeActivity.this, ClassroomsListActivity.class);
                startActivity(ClassroomActivity);
            }
        });
    }


}