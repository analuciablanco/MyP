package com.example.myp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myp.FireBase.models.Classroom;
import com.example.myp.FireBase.models.ClassRoomMember;
import com.example.myp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import org.apache.commons.lang3.RandomStringUtils;

// Clase para Crear Aulas
public class CreateClassroomActivity extends AppCompatActivity {

    // Declaration of Views
    Spinner GradeSpinner;       // Spinner
    Spinner GroupSpinner;       // Spinner
    EditText editText_School;   // EditText
    Button create_button;       // Button

    Classroom classroom = new Classroom();
    DocumentReference newUserRef, newMemberRef;


    // Database variable Declaration
    private FirebaseFirestore aulaDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);

        // Hide Keyboard upon startup
        hideSoftKeyboard();

        // Initialization of Java variables to their analogous XML elements
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
                // Hide Keyboard
                hideSoftKeyboard();

                // Disable the button to avoid multiple classroom creation
                create_button.setClickable(false);

                // Validate if all fields are correctly filled.
                if (attemptClassroomRegistration()) {
                    String grade = GradeSpinner.getSelectedItem().toString();
                    String group = GroupSpinner.getSelectedItem().toString();
                    String school_name = editText_School.getText().toString().trim();

                    // Insertion of Classroom data to FireBase
                    String classroomStatus = "ACTIVO";
                    insertClassroom(grade, group, school_name, classroomStatus);
                }
            }
        });
    }

    // Classroom insertion to FireBase
    private void insertClassroom(final String grade, final String group, final String school_name, final String classroomStatus) {
        //final Classroom classroom = new Classroom();
        classroom.setGrade(grade);
        classroom.setGroup(group);
        classroom.setSchool_name(school_name);
        classroom.setCode_teacher(RandomStringUtils.random(6, true, true));
        classroom.setCode_parent(RandomStringUtils.random(6, true, true));
        classroom.setStatus(classroomStatus);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        aulaDB.setFirestoreSettings(settings);

        newUserRef = aulaDB
                .collection(getString(R.string.collection_classrooms))
                .document();
        classroom.setID(newUserRef.getId());

        newUserRef.set(classroom).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Classroom created. Print success message
                        showMessage("Creación de aula exitosa.");
                        String member_status = "ACTIVO";
                        String admin = "admin";
                        insertMemberAdmin(admin, newUserRef, member_status);

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
        // Variable to validate that all fields are correctly filled
        boolean authentication = true;

        // This sends your screen to the first empty field from the top.
        View focusView = null;

        String grade = GradeSpinner.getSelectedItem().toString();
        String group = GroupSpinner.getSelectedItem().toString();
        String schoolName = editText_School.getText().toString().trim();

        // If any of the fields is empty: print message, avoid navigation to next
        // screen and enable the classroom creation button once more to try again.
        if (TextUtils.isEmpty(schoolName)) {
            focusView = editText_School;
            showMessage("Necesitas llenar todos los  campos para crear el aula.");
            focusView.requestFocus();

            authentication = false;
            create_button.setClickable(true);
        }

        // If everything is correct, authentication will be true and program continues.
        return authentication;
    }

    // Function to insert the class's creator as an admin
    private void insertMemberAdmin(final String adminRole, DocumentReference newUserRef, final String member_status) {
        ClassRoomMember classroomMember = new ClassRoomMember();

        classroomMember.setUser_id(FirebaseAuth.getInstance().getUid());
        classroomMember.setRole(adminRole);
        classroomMember.setMember_status(member_status);

        newMemberRef = newUserRef
                .collection(getString(R.string.collection_classrom_members)).document();

        classroomMember.setMember_id(newMemberRef.getId());

        newMemberRef.set(classroomMember).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Navigate to your invitation code and close this screen.
                navShareCode();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Fallo en el registro del aula.");
            }
        });
    }

    // Navigate to the share code screen after a classroom's created and close this one.
    private void navShareCode()
    {
        Intent ShareCode = new Intent(CreateClassroomActivity.this, ShareCodeActivity.class);
        ShareCode.putExtra("classroom_document_ID", classroom.getID());
        startActivity(ShareCode);
        finish();
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

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
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