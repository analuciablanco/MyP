package com.example.myp;

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


public class createClassroom extends AppCompatActivity {

    //Declaracion KEYS
    private static final String GRADE_KEY = "aula_grado",
            GROUP_KEY = "aula_grupo",
            SCHOOL_NAME_KEY = "aula_escuelaNombre";


    //Declaration Spinner
    Spinner spinnerGrado;
    Spinner spinnerGrupo;

    //Declaration EditText
    EditText editTextEscuela;

    //Declaration Button
    Button buttonCrearAula;

    //Declaracion variable base de datos
    private FirebaseFirestore aulaDB = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();


        buttonCrearAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attempRegistrationAulas()) {
                    String grado = spinnerGrado.getSelectedItem().toString();
                    String grupo = spinnerGrado.getSelectedItem().toString();
                    String nombreEscuela = editTextEscuela.getText().toString().trim();

                    insertarAula(grado, grupo, nombreEscuela);
                }
            }
        });


    }

    private void initViews() {
        spinnerGrado = (Spinner) findViewById(R.id.Grado);
        spinnerGrupo = (Spinner) findViewById(R.id.Grupo);

        editTextEscuela = (EditText) findViewById(R.id.editText_Escuela);

        buttonCrearAula = (Button) findViewById(R.id.button_createRoom);
    }

    private boolean attempRegistrationAulas() {
        boolean autenticacion = false;

        String grado = spinnerGrado.getSelectedItem().toString();
        String grupo = spinnerGrado.getSelectedItem().toString();
        String nombreEscuela = editTextEscuela.getText().toString().trim();

        if (!TextUtils.isEmpty(grado) || !TextUtils.isEmpty(grupo) || !TextUtils.isEmpty(nombreEscuela)) {
            showMessage("Necesitas llenar todos los  campos para crear el aula.");
        } else {
            autenticacion = true;
        }

        return autenticacion;
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void insertarAula(String grado, String grupo, String nombreEscuela) {
        Map<String, Object> classroom = new HashMap<>();
        classroom.put(GRADE_KEY, grado);
        classroom.put(GROUP_KEY, grupo);
        classroom.put(SCHOOL_NAME_KEY, nombreEscuela);

        aulaDB.collection("aula").document().set(classroom)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Registro de usuario exitoso.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Fallo en el registro del aula.");
            }
        });
    }
}


