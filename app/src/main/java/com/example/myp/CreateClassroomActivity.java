package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateClassroomActivity extends AppCompatActivity {

    private static final String GRADE_KEY = "aula_grado",
            GROUP_KEY = "aula_grupo",
            SCHOOL_NAME_KEY = "aula_escuelaNombre",
            STATUS_KEY = "aula_status";

    // Declaration of Views
    Spinner GradeSpinner;       // Spinner
    Spinner GroupSpinner;       // Spinner
    EditText editText_School;   // EditText
    Button create_button;        // Button

    // Declaracion variable base de datos
    private FirebaseFirestore aulaDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);
        create_button = findViewById(R.id.create_button);
        initViews();
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (attempRegistrationAulas()) {
                    String grado = GradeSpinner.getSelectedItem().toString();
                    String grupo = GroupSpinner.getSelectedItem().toString();
                    String nombreEscuela = editText_School.getText().toString().trim();

                    insertarAula(grado, grupo, nombreEscuela, 1);
                }
            }
        });
    }

    private void insertarAula(final String grado, final String grupo, final String nombreEscuela, final int statusAula) {
        Map<String, Object> classroom = new HashMap<>();
        classroom.put(GRADE_KEY, grado);
        classroom.put(GROUP_KEY, grupo);
        classroom.put(SCHOOL_NAME_KEY, nombreEscuela);
        classroom.put(STATUS_KEY, statusAula);

        aulaDB.collection("aula").document().set(classroom)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Registro de usuario exitoso.");
                        Intent intent = new Intent(CreateClassroomActivity.this, ClassroomsListActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Fallo en el registro del aula.");
            }
        });
    }


    private boolean attempRegistrationAulas() {
        boolean authentication = true;

        String grade = GradeSpinner.getSelectedItem().toString();
        String group = GroupSpinner.getSelectedItem().toString();
        String schoolName = editText_School.getText().toString().trim();

        if ((TextUtils.isEmpty(grade) || TextUtils.isEmpty(group) || TextUtils.isEmpty(schoolName))) {
            showMessage("Necesitas llenar todos los  campos para crear el aula.");
            authentication = false;
        }

        return authentication;
    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initViews() {
        GroupSpinner = findViewById(R.id.GroupSpinner);
        GradeSpinner = findViewById(R.id.GradeSpinner);
        editText_School = findViewById(R.id.editText_School);
        create_button = findViewById(R.id.create_button);
    }
}