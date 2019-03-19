package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myp.R;

// Clase para unirte a Salones
public class JoinClassroomActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_classroom);

        // Button to navigate to classrooms list
        final Button btn_Accept_Code = findViewById(R.id.btn_Accept_Code);
        btn_Accept_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Classrooms List
                navClassroomsList();
            }
        });

        // ActionListener to accept Enter input on keyboard
        // (from an editText XML element) as a press on the JoinClassroom button
        EditText textInputCode = findViewById(R.id.textInputCode);
        textInputCode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_Accept_Code.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void navClassroomsList(){
        Intent classroomsList = new Intent(JoinClassroomActivity.this, ClassroomsListActivity.class);
        startActivity(classroomsList);
        finish();
    }

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}