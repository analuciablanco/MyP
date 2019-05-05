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

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.google.firebase.auth.FirebaseUser;
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

//import {FirestoreSQL} from 'firesql';

import java.lang.reflect.Array;
import java.util.ArrayList;

// Clase para unirte a Salones
public class JoinClassroomActivity extends AppCompatActivity {
    //Declaration of views
    EditText textInputCode;
    Button btn_Accept_Code;

    String TAG = "ACCESS CODE:";

    // Vars
    private FirebaseFirestore mDb;
    private ArrayList<ClassRoomMember> classroomMemberArray = new ArrayList<>();
    private ArrayList<User> usersArrayList = new ArrayList<>();
    private ArrayList<Classroom> classroomArrayList = new ArrayList<>();
    private ListenerRegistration mClassromEventListener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private boolean memberExists = false;

    private Classroom mClassRoom;
    private ClassRoomMember mclassroomMember;
    Query mQuery;


    String member_status = "ACTIVO";
    String memberRoleParent = "member";
    String memberRoleTeacher = "admin";
    String[] arrayCodeParent;
    String[] arrayCodeTeacher;
    String[] arrayIdMemberUser;
    String[] array_member_full_name;
    private String currentCodigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_classroom);
        initViews();

        mDb = FirebaseFirestore.getInstance();
        //getClassrooms();
        //getUsers();

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
        btn_Accept_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (attempJoinClassroom()) {
                    String accessCode = textInputCode.getText().toString();
                    getClassrooms(accessCode);

                    //verifyAccessCode(accessCode);
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

        classroomRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < classroomArrayList.size(); i++) {
                        if (accessCode.equals(arrayCodeParent[i])) {
                            String classroomID = classroomArrayList.get(i).getID();
                            getClassroomMembers(classroomID);
                            addClassroomMember(classroomID, memberRoleParent, array_member_full_name);
                        } else if (accessCode.equals(arrayCodeTeacher[i])) {
                            String classroomID = classroomArrayList.get(i).getID();
                            getClassroomMembers(classroomID);
                            addClassroomMember(classroomID, memberRoleTeacher, array_member_full_name);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error, no se encontro ningun aula. (ERROR REAL =" + e.getMessage());
            }
        });
    }

    private void addClassroomMember(final String classroomID, final String memberRole, final String[] array_member_full_name) {
        final ClassRoomMember classRoomMember = new ClassRoomMember();
        classRoomMember.setUser_id(FirebaseAuth.getInstance().getUid());
        classRoomMember.setRole(memberRole);
        classRoomMember.setMember_status(member_status);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        classRoomMember.setMember_full_name(firebaseUser.getDisplayName());
        compareIDToGetName();

        // This string specifies the ID of the user trying to join the classroom
        final String uid = FirebaseAuth.getInstance().getUid();

        arrayCodeTeacher = new String[classroomArrayList.size()];
        for (int i = 0; i < classroomMemberArray.size(); i++) {
            String UIDmembers = arrayIdMemberUser[i];
            Log.d(TAG, "UID "+ UIDmembers);
            if (uid.equals(UIDmembers)) {
                memberExists = true;
                i += classroomMemberArray.size();
            }
            else memberExists = false;
        }

        if (!memberExists) {
            DocumentReference newMemberRef = mDb.collection(getString(R.string.collection_classrooms)).
                    document(classroomID).collection(getString(R.string.collection_classrom_members)).document();

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
        } else {
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

    private void getClassrooms(final String accessCode) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.collection(getString(R.string.collection_classrooms)).whereEqualTo("code_parent", accessCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        classroomArrayList.clear();
                       if (task.isSuccessful()){
                           for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                               Classroom classroom = documentSnapshot.toObject(Classroom.class);
                               classroomArrayList.add(classroom);
                           }
                           //Log.d(TAG, " ID:" + classroomArrayList.get(0).getID() + " code parent: " + classroomArrayList.get(0).getCode_parent());
                       }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error, no se econtro ningun aula. (ERROR REAL =" + e.getMessage());
            }
        });
        /*mDb.collection(getString(R.string.collection_classrooms)).whereEqualTo("code_teacher", accessCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //classroomArrayList.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Classroom classroom = documentSnapshot.toObject(Classroom.class);
                                classroomArrayList.add(classroom);
                            }
                           //Log.d(TAG, " ID:" + classroomArrayList.get(classroomArrayList.size()).getID() + " code teacher: " + classroomArrayList.get(classroomArrayList.size()).getCode_parent());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error, no se econtro ningun aula. (ERROR REAL =" + e.getMessage());
            }
        });*/
    }

    // All of this bullshit is simply to get the User Name of the desired member in the classroom
    private void getUsers() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference classroomRef = mDb.collection(getString(R.string.collection_users));
        classroomRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        usersArrayList.add(user);
                    }
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
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "No e ncontro nada.");
            }
        });
    }

    // Compare the classroom members' IDs with the Users' IDs to get the name from user field full_name
    private void compareIDToGetName() {

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

    // Function to (supposedly) hide the keyboard upon use.
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