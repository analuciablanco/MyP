package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

// Clase que enlista todos los salones
public class ClassroomsListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms_list);

        //Disable backButton from action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        // Function to navigate to a classroom
        LinearLayout chatList = findViewById(R.id.btn_first_classroom);
        chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatList = new Intent(ClassroomsListActivity.this, ChatListActivity.class);
                startActivity(chatList);
            }
        });

        // Function to navigate to a classroom
        LinearLayout chatList2 = findViewById(R.id.btn_second_classroom);
        chatList2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatList = new Intent(ClassroomsListActivity.this, ChatListActivity.class);
                startActivity(chatList);
            }
        });

        // Function to navigate to a classroom
        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton_classrooms_list);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHome = new Intent(ClassroomsListActivity.this, HomeActivity.class);
                startActivity(goToHome);
            }
        });
    }

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    // This class adds the button delete
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_button_classrooms,menu);
        return super.onCreateOptionsMenu(menu);
    }

}

