package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// Clase para Crear Aulas
public class CreateClassroomActivity extends AppCompatActivity {

    // Database fields declaration
    private static final String GRADE_KEY = "aula_grado",
            GROUP_KEY = "aula_grupo",
            SCHOOL_NAME_KEY = "aula_escuelaNombre",
            STATUS_KEY = "aula_status";

    // Declaration of Views
    Spinner GradeSpinner;       // Spinner
    Spinner GroupSpinner;       // Spinner
    EditText editText_School;   // EditText
    Button create_button;       // Button

    // Database variable Declaration
    private FirebaseFirestore aulaDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);
        initViews();

        // ActionListener to accept Enter input on keyboard
        // (from an editText XML element) as a press on the login button
        editText_School = findViewById(R.id.editText_School);
        editText_School.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    create_button.performClick();
                    return true;
                }
                return false;
            }
        });

        // Button for classroom creation
        create_button = findViewById(R.id.create_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Disable the button to avoid multiple classroom creation
                create_button.setClickable(false);

                // Validate if all fields are correctly filled.
                if (attemptClassroomRegistration()) {
                    String grade = GradeSpinner.getSelectedItem().toString();
                    String group = GroupSpinner.getSelectedItem().toString();
                    String school_name = editText_School.getText().toString().trim();

                    // Insertion of Classroom data to FireBase
                    insertClassroom(grade, group, school_name, 1);
                }
            }
        });
    }

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    // Classroom insertion to FireBase
    private void insertClassroom(final String grado, final String grupo, final String nombreEscuela, final int statusAula) {
        Map<String, Object> classroom = new HashMap<>();
        classroom.put(GRADE_KEY, grado);
        classroom.put(GROUP_KEY, grupo);
        classroom.put(SCHOOL_NAME_KEY, nombreEscuela);
        classroom.put(STATUS_KEY, statusAula);

        aulaDB.collection("aula").document().set(classroom)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Classroom created. Print success message
                        showMessage("Creación de aula exitosa.");

                        // Navigate to your invitation code and close this screen.
                        Intent intent = new Intent(CreateClassroomActivity.this, ShareCodeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Classroom creation failed. Print failure alert.
                showMessage("Fallo en el registro del aula.");
            }
        });
    }

    // Classroom registration validation function
    private boolean attemptClassroomRegistration() {
        boolean authentication = true;

        String grade = GradeSpinner.getSelectedItem().toString();
        String group = GroupSpinner.getSelectedItem().toString();
        String schoolName = editText_School.getText().toString().trim();

        // If any of the fields is empty: print message, avoid navigation to next
        // screen and enable the classroom creation button once more to try again.
        if ((TextUtils.isEmpty(grade) || TextUtils.isEmpty(group) || TextUtils.isEmpty(schoolName))) {
            showMessage("Necesitas llenar todos los  campos para crear el aula.");
            authentication = false;
            create_button.setClickable(true);
        }

        // If everything is correction, authentication will be true and program continues.
        return authentication;
    }

    // This function is used to easily set Toast print messages
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    // This method is used to connect XML elements to their Objects
    private void initViews() {
        GroupSpinner = findViewById(R.id.GroupSpinner);
        GradeSpinner = findViewById(R.id.GradeSpinner);
        editText_School = findViewById(R.id.editText_School);
        create_button = findViewById(R.id.create_button);
    }
}