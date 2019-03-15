package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

// Clase para compartir código de invitación al aula
public class ShareCodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharecode);

        // Button to navigate to classrooms list
        Button button_to_classrooms = findViewById(R.id.button_to_classrooms);
        button_to_classrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Classrooms List
                Intent intent = new Intent(ShareCodeActivity.this, ClassroomsListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}