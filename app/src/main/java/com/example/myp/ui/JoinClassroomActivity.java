package com.example.myp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myp.R;

// Clase para unirte a Salones
public class JoinClassroomActivity extends AppCompatActivity {

    // Declaration of XML Views
    Button btn_Accept_Code;
    EditText textInputCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_classroom);

        // Hide keyboard upon startup
        hideSoftKeyboard();

        // Initialization of XML Views as Java variables
        initViews();

        // Button to navigate to classrooms list
        btn_Accept_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                // Go to Classrooms List
                navClassroomsList();
            }
        });

        // ActionListener to accept Enter input on keyboard
        // (from an editText XML element) as a press on the JoinClassroom button
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

    // Navigate to the Classrooms' List screen
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

    // Initialization of XML Views as Java variables
    private void initViews(){
        btn_Accept_Code = findViewById(R.id.btn_Accept_Code);
        textInputCode = findViewById(R.id.textInputCode);
    }

    // Function to hide the keyboard upon use.
    private void hideSoftKeyboard(){
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}