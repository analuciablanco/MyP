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
                          SCHOOL_NAME_KEY = "aula_escuelaNombre";


    //Declaration Spinner
    Spinner spinnerGrado;
    Spinner spinnerGrupo;

    //Declaration EditText
    EditText editTextEscuela;



    //Declaracion variable base de datos
    private FirebaseFirestore aulaDB = FirebaseFirestore.getInstance();

    //Declaracion Button Aula
    Button createbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);
            createbutton = findViewById(R.id.createbutton);
        initViews();
    createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (attempRegistrationAulas()) {
                    String grado = spinnerGrado.getSelectedItem().toString();
                    String grupo = spinnerGrupo.getSelectedItem().toString();
                    String nombreEscuela = editTextEscuela.getText().toString().trim();

                    insertarAula(grado, grupo, nombreEscuela);
                }

            }
        });
    }

    private void insertarAula(final String grado, final String grupo, final String nombreEscuela) {
        Map<String, Object> classroom = new HashMap<>();
        classroom.put(GRADE_KEY, grado);
        classroom.put(GROUP_KEY, grupo);
        classroom.put(SCHOOL_NAME_KEY, nombreEscuela);

        aulaDB.collection("aula").document().set(classroom)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Registro de usuario exitoso.");
                        Intent intent = new Intent(CreateClassroomActivity.this, ClassroomsActivity.class);
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
        boolean autenticacion = true;

        String grado = spinnerGrado.getSelectedItem().toString();
        String grupo = spinnerGrupo.getSelectedItem().toString();
        String nombreEscuela = editTextEscuela.getText().toString().trim();

        if ((TextUtils.isEmpty(grado) || TextUtils.isEmpty(grupo) || TextUtils.isEmpty(nombreEscuela))) {
            showMessage("Necesitas llenar todos los  campos para crear el aula.");
            autenticacion = false;
        }

        return autenticacion;
    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initViews() {
        spinnerGrado = (Spinner) findViewById(R.id.Grupo);
        spinnerGrupo = (Spinner) findViewById(R.id.Grado);

        editTextEscuela = (EditText) findViewById(R.id.editText_Escuela);

        createbutton= (Button) findViewById(R.id.createbutton);
    }
}