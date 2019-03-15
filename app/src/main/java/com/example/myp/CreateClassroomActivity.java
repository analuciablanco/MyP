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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class CreateClassroomActivity extends AppCompatActivity {

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
                    String statusAula = "ACTIVO";
                    insertarAula(grado, grupo, nombreEscuela, statusAula);
                }
            }
        });
    }

    private void insertarAula(final String grado, final String grupo, final String nombreEscuela, final String statusAula) {
        final ClassRoom classRoom = new ClassRoom();
            //classRoom.setID(FirebaseFirestore.getInstance().collection("Classrooms").getId());
            classRoom.setGrade(grado);
            classRoom.setGroup(grupo);
            classRoom.setSchool_name(nombreEscuela);
            classRoom.setStatus(statusAula);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        aulaDB.setFirestoreSettings(settings);

        final DocumentReference newUserRef = aulaDB
                .collection(getString(R.string.collection_classrooms))
                .document();
        classRoom.setID(newUserRef.getId());

        newUserRef.set(classRoom)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Registro de usuario exitoso.");
                        /*Intent intent = new Intent(CreateClassroomActivity.this, ClassroomsListActivity.class);
                        startActivity(intent);*/
                       // newUserRef.collection(getString(R.string.collection_classrom_members)).document().set()
                        String admin = "admin";
                        insertMemberAdmin(classRoom.getID(),admin, newUserRef);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Fallo en el registro del aula.");
            }
        });




    }

    private void insertMemberAdmin(final String IdDocument, final String adminRole, DocumentReference newUserRef) {
       ClassRoomMember classRoomMember = new ClassRoomMember();
       classRoomMember.setUser_id(FirebaseAuth.getInstance().getUid());
       classRoomMember.setRole(adminRole);

        final DocumentReference newMemberRef = newUserRef
                .collection(getString(R.string.collection_classrom_members)).document();

        classRoomMember.setMember_id(newMemberRef.getId());

       newMemberRef.set(classRoomMember).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
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