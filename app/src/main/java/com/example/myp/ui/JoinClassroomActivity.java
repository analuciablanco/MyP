package com.example.myp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myp.FireBase.models.User;
import com.example.myp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.myp.FireBase.models.Classroom;
import com.example.myp.FireBase.models.ClassRoomMember;

import java.util.ArrayList;

// Clase para unirte a Salones
public class JoinClassroomActivity extends AppCompatActivity {
    //Declaration of views
    EditText textInputCode;
    Button btn_Accept_Code;

    String TAG = "ACCESS CODE:";

    //vars
    private FirebaseFirestore mDb;
    private ArrayList<ClassRoomMember> classroomMemberArray = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Classroom> classroomArrayList = new ArrayList<>();
    private ListenerRegistration mClassromEventListener;

    private Classroom mClassRoom;
    private ClassRoomMember mclassroomMember;
    Query mQuery;

    String member_status = "ACTIVO";
    String memberRoleParent = "member";
    String memberRoleTeacher = "teacher";
    String[] arrayCodeParent;
    String[] arrayCodeTeacher;
    String[] arrayIdMemberUser;
    private String currentCodigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_classroom);
        initViews();
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
        //initViews();
        mDb = FirebaseFirestore.getInstance();
        getClassrooms();
        btn_Accept_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attempJoinClassroom()){
                    String accessCode = textInputCode.getText().toString();
                    Log.d(TAG, accessCode);
                    verifyAccessCode(accessCode);
                    // Go to Classrooms List
              /* Intent intent = new Intent(JoinClassroomActivity.this, ClassroomsListActivity.class);
                startActivity(intent);
                finish();*/
                }

            }
        });

    }

    private void verifyAccessCode(final String accessCode) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference classroomRef = mDb.collection(getString(R.string.collection_classrooms));
        for (int i = 0; i < classroomArrayList.size(); i++){
            if(accessCode.equals(arrayCodeParent[i])){
                String classroomID = classroomArrayList.get(i).getID();
                getClassroomMembers(classroomID);
                addClassroomMember(classroomID, memberRoleParent);
            }
            if(accessCode.equals(arrayCodeTeacher[i])){
                String classroomID = classroomArrayList.get(i).getID();
                getClassroomMembers(classroomID);
                addClassroomMember(classroomID, memberRoleTeacher);

            }

        }
    }
    private void addClassroomMember(final String classroomID, final String memberRole) {
        ClassRoomMember classRoomMember = new ClassRoomMember();
        classRoomMember.setUser_id(FirebaseAuth.getInstance().getUid());
        classRoomMember.setRole(memberRole);
        classRoomMember.setMember_status(member_status);
        String uid = (String)FirebaseAuth.getInstance().getUid();


        Log.d(TAG, uid);
        showMessage(uid);
        int memberDontExist = 1;
        for(int i=0; i<classroomMemberArray.size(); i++){
            String UIDmembers = arrayIdMemberUser[i];
            if(uid.equals(UIDmembers)){
                memberDontExist =0;
                i= classroomMemberArray.size() + 1;
            }
        }
        if (memberDontExist == 1){
            DocumentReference newMemberRef = mDb.collection(getString(R.string.collection_classrooms)).
                    document(classroomID).collection(getString(R.string.collection_classrom_members)).document();
            classRoomMember.setMember_id(newMemberRef.getId());
            newMemberRef.set(classRoomMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(JoinClassroomActivity.this, ClassroomsListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showMessage("Fallo en unirse al aula.");
                }
            });
        }
        else{
            showMessage("Ya perteneces a esta clase.");
        }
    }

   private boolean attempJoinClassroom() {
        boolean authentication = true;

        String accessCode = textInputCode.getText().toString().trim();

        if ((TextUtils.isEmpty(accessCode))) {
            showMessage("Necesitas llenar el campo para participar en el aula.");
            authentication = false;
        }

        return authentication;
    }

    private void getClassrooms() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference classroomRef = mDb.collection(getString(R.string.collection_classrooms));
        classroomRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Classroom classroom = documentSnapshot.toObject(Classroom.class);
                        classroomArrayList.add(classroom);
                    }
                }
                arrayCodeTeacher = new String[classroomArrayList.size()];
                arrayCodeParent = new String[classroomArrayList.size()];
                for (int i = 0; i < classroomArrayList.size(); i++) {
                    arrayCodeParent[i] = (String)classroomArrayList.get(i).getCode_parent();
                    arrayCodeTeacher[i] = (String)classroomArrayList.get(i).getCode_teacher();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error, no se econtro ningun aula. (ERROR REAL =" + e.getMessage());
            }
        });
    }
    private void getClassroomMembers(final String classroomID){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference
                classroomMembersRef = mDb.collection(getString(R.string.collection_classrooms)).document(classroomID).collection(getString(R.string.collection_classrom_members));
        // CollectionReference classroomMembersRef = mDb.collection(getString(R.string.collection_classrom_members));
        classroomMembersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        ClassRoomMember classRoomMember = doc.toObject(ClassRoomMember.class);
                        classroomMemberArray.add(classRoomMember);
                    }
                }
                arrayIdMemberUser = new String[classroomMemberArray.size()];
                for(int i=0; i<classroomMemberArray.size(); i++){
                    arrayIdMemberUser[i] = (String)classroomMemberArray.get(i).getUser_id();
                    String currentCodigo = (String)arrayIdMemberUser[i];
                    Log.d(TAG, "UID:" + currentCodigo);
                    //Log.d(TAG, "UID: " + i + ":" + arrayIdMemberUser[i]);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "No encontro nada.");
            }
        });
    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initViews() {
        textInputCode = findViewById(R.id.textInputCode);
        btn_Accept_Code = findViewById(R.id.btn_Accept_Code);
    }
    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }


/*
    /

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

                if(attempJoinClassroom()){
                    String accessCode = textInputCode.getText().toString();
                    verifyAccessCode(accessCode);
                    // Navigate to Classrooms List
                    navClassroomsList();
                }
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

        mDb = FirebaseFirestore.getInstance();

        // Function to fill the array list with all classrooms on FireBase
        getClassrooms();

        btn_Accept_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attempJoinClassroom()){
                    String accessCode = textInputCode.getText().toString();
                    Log.d(TAG, accessCode);
                    verifyAccessCode(accessCode);
                    // Go to Classrooms List
                    navClassroomsList();
                }

            }
        });
    }

    // Validates the code with the one stored in FireBase
    private void verifyAccessCode(final String accessCode) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference classroomRef = mDb.collection(getString(R.string.collection_classrooms));
        for (int i = 0; i < classRoomArrayList.size(); i++){
            if(accessCode.equals(arrayCodeParent[i])){
                String classroomID = classRoomArrayList.get(i).getID();
                getClassroomMembers(classroomID);
                addClassroomMember(classroomID, memberRoleParent);
            }
            if(accessCode.equals(arrayCodeTeacher[i])){
                String classroomID = classRoomArrayList.get(i).getID();
                getClassroomMembers(classroomID);
                addClassroomMember(classroomID, memberRoleTeacher);
            }
        }
    }

    // Adds you to the classroom once the code has been validated
    private void addClassroomMember(final String classroomID, final String memberRole) {
        ClassRoomMember classRoomMember = new ClassRoomMember();
        classRoomMember.setUser_id(FirebaseAuth.getInstance().getUid());
        classRoomMember.setRole(memberRole);
        classRoomMember.setMember_status(member_status);
        String uid = (String)FirebaseAuth.getInstance().getUid();
        int memberDontExist = 1;
        for(int i=0; i<classRoomMemberArray.size(); i++){
            String UIDmembers = arrayIdMemberUser[i];
            if(uid.equals(UIDmembers)){
                memberDontExist =0;
                i= classRoomMemberArray.size() + 1;
            }
        }
        if (memberDontExist == 0){
            DocumentReference newMemberRef = mDb.collection(getString(R.string.collection_classrooms)).
                    document(classroomID).collection(getString(R.string.collection_classrom_members)).document();
            classRoomMember.setMember_id(newMemberRef.getId());
            newMemberRef.set(classRoomMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(JoinClassroomActivity.this, ClassroomsListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showMessage("Fallo en unirse al aula.");
                }
            });
        }
        else{
            showMessage("Ya perteneces a esta clase.");
        }
    }

    // Front end validation
    private boolean attempJoinClassroom() {
        boolean authentication = true;

        String accessCode = textInputCode.getText().toString().trim();

        if ((TextUtils.isEmpty(accessCode))) {
            showMessage("Necesitas llenar el campo para participar en el aula.");
            authentication = false;
        }

        return authentication;
    }

    // Fill of the ArrayList with classrooms on FireBase
    private void getClassrooms() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference classroomRef = mDb.collection(getString(R.string.collection_classrooms));
        classroomRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Classroom classroom = documentSnapshot.toObject(Classroom.class);
                        classRoomArrayList.add(classroom);
                    }
                }
                arrayCodeTeacher = new String[classRoomArrayList.size()];
                arrayCodeParent = new String[classRoomArrayList.size()];
                for (int i = 0; i < classRoomArrayList.size(); i++) {
                    arrayCodeParent[i] = (String)classRoomArrayList.get(i).getCode_parent();
                    arrayCodeTeacher[i] = (String)classRoomArrayList.get(i).getCode_teacher();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error, no se econtro ningún aula. (ERROR REAL =" + e.getMessage());
            }
        });
    }

    // This function makes it impossible to duplicate yourself in a classroom
    private void getClassroomMembers(final String classroomID){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference
                classroomMembersRef = mDb.collection(getString(R.string.collection_classrooms)).document(classroomID).collection(getString(R.string.collection_classrom_members));
        // CollectionReference classroomMembersRef = mDb.collection(getString(R.string.collection_classrom_members));
        classroomMembersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        ClassRoomMember classRoomMember = doc.toObject(ClassRoomMember.class);
                        classRoomMemberArray.add(classRoomMember);
                    }
                }
                arrayIdMemberUser = new String[classRoomMemberArray.size()];
                for(int i=0; i<classRoomMemberArray.size(); i++){
                    arrayIdMemberUser[i] = (String)classRoomMemberArray.get(i).getUser_id();
                    String currentCodigo = (String)arrayIdMemberUser[i];
                    Log.d(TAG, "UID:" + currentCodigo);
                    //Log.d(TAG, "UID: " + i + ":" + arrayIdMemberUser[i]);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "No encontro nada.");
            }
        });
    }

    // Makes Toasts more accesible
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
    */
}