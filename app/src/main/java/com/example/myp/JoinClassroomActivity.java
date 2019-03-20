package com.example.myp;

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

import java.util.ArrayList;

// Clase para unirte a Salones
public class JoinClassroomActivity extends AppCompatActivity {
    //Declaration of views
    EditText textInputCode;
    Button btn_Accept_Code;

    String TAG = "ACCESS CODE:";

    //vars
    private FirebaseFirestore mDb;
    private ArrayList<ClassRoomMember> classRoomMemberArray = new ArrayList<>();
    private ArrayList<ClassRoom> classRoomArrayList = new ArrayList<>();
    private ListenerRegistration mClassromEventListener;

    private ClassRoom mClassRoom;
    private ClassRoomMember mclassRoomMember;
    Query mQuery;

    String member_status = "ACTIVO";
    String memberRoleParent = "member";
    String memberRoleTeacher = "teacher";
    String[] arrayCodeParent;
    String[] arrayCodeTeacher;
    String[] arrayIdMemberUser;


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
        //getClassroomMembers();
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
       for (int i = 0; i < classRoomArrayList.size(); i++){
           if(accessCode.equals(arrayCodeParent[i])){
                String classroomID = classRoomArrayList.get(i).getID();
              // getClassroomMembers(classroomID);
                addClassroomMember(classroomID, memberRoleParent, i);
            }
          if(accessCode.equals(arrayCodeTeacher[i])){
               String classroomID = classRoomArrayList.get(i).getID();
               // getClassroomMembers(classroomID);
               addClassroomMember(classroomID, memberRoleTeacher, i);

           }

        }
    }
    private void addClassroomMember(final String classroomID, final String memberRole, final int i) {
        ClassRoomMember classRoomMember = new ClassRoomMember();
        classRoomMember.setUser_id(FirebaseAuth.getInstance().getUid());
        classRoomMember.setRole(memberRole);
        classRoomMember.setMember_status(member_status);
        //if (attempJoinClassroomMember(i)){
            final DocumentReference newMemberRef = mDb.collection(getString(R.string.collection_classrooms)).
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
    /*else{
            showMessage("Ya perteneces a esta clase.");
        }
  }*/

    private boolean attempJoinClassroomMember(final int i ) {
        boolean memberDontExist = false ;
        String uid = FirebaseAuth.getInstance().getUid();
       if(uid.equals(arrayIdMemberUser[i])){
           memberDontExist = true;
       }
       return  memberDontExist;
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
                        ClassRoom classRoom = documentSnapshot.toObject(ClassRoom.class);
                        classRoomArrayList.add(classRoom);
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
                for(int i=0; i<classRoomMemberArray.size(); i++){
                    arrayIdMemberUser[i] = (String)classRoomMemberArray.get(i).getUser_id();
                    Log.d(TAG, "UID: " + i + ":" + arrayIdMemberUser[i]);
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
}