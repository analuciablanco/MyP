package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.myp.R;

// Pantalla principal tras iniciar sesión
public class HomeActivity extends AppCompatActivity {

    // Linear Layout (XML) declaration
    LinearLayout createClassroom;
    LinearLayout joinClassroom;
    LinearLayout backToChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Function to initialize java variables as their analogous XML elements
        initViews();

        // Function to delete the top back arrow on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Button to Create Classrooms
        createClassroom.setClickable(true);
        createClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable the button to avoid double clicks
                createClassroom.setClickable(false);
                // Navigate to create classrooms' screen
                navCreateClassrooms();
            }
        });

        // Button to Join Classrooms
        joinClassroom.setClickable(true);
        joinClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the button to avoid double clicks
                joinClassroom.setClickable(false);
                // Navigate to join classrooms' screen
                navJoinClassrooms();
            }
        });

        // Button to check your current made classrooms
        backToChatList.setClickable(true);
        backToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the button to avoid double clicks
                backToChatList.setClickable(false);
                // Navigate to classrooms' list screen
                onBackPressed();
            }
        });
    }

    // XML-Java connection
    private void initViews(){
        createClassroom = findViewById(R.id.createClassroomButton);
        joinClassroom = findViewById(R.id.joinClassroomButton);
        backToChatList = findViewById(R.id.backChatListButton);
    }

    // Navigation to create classrooms' screen
    private void navCreateClassrooms(){
        Intent createClassroom = new Intent(HomeActivity.this, CreateClassroomActivity.class);
        startActivity(createClassroom);
    }

    // Navigation to join classrooms' screen
    private void navJoinClassrooms(){
        Intent joinClassroom = new Intent(HomeActivity.this, JoinClassroomActivity.class);
        startActivity(joinClassroom);
    }

    // Function (different from finish()) to navigate back to parent layout (classrooms' list screen)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent classroomsList = new Intent(HomeActivity.this, ClassroomsListActivity.class);
        startActivity(classroomsList);
        finish();
    }
}