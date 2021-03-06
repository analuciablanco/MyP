package com.example.myp.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;

import javax.annotation.Nullable;

//Class to share the code
public class ShareCodeActivity extends AppCompatActivity {
    // Declaration of Java-XML views
    TextView    sharecode_textview_parents,
                sharecode_textview_teachers;

    String      sharecode_textview_insertcode_parents,
                sharecode_textview_insertcode_teachers,
                code_Parents,
                code_Teachers;

    ImageButton sharecode_imagebutton_copy_parents,
                sharecode_imagebutton_copy_teachers;

    ImageButton sharecode_imagebutton_share_parents,
                sharecode_imagebutton_share_teachers;

    private FirebaseFirestore dbFireStore = FirebaseFirestore.getInstance();

    DocumentReference shareCodeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharecode);

        Intent ShareCode = getIntent();
        String classroom_document_ID = ShareCode.getStringExtra("classroom_document_ID");

        shareCodeRef = dbFireStore
                .collection(getString(R.string.collection_classrooms))
                .document(classroom_document_ID);

        // Link class TextView variables with layout TextView id
        sharecode_textview_parents = findViewById(R.id.sharecode_textview_insertcode_parents);
        sharecode_textview_teachers = findViewById(R.id.sharecode_textview_insertcode_teachers);

        // Link class ImageButton variables with layout ImageButton id
        sharecode_imagebutton_copy_parents = findViewById(R.id.sharecode_imagebutton_copy_parents);
        sharecode_imagebutton_copy_teachers = findViewById(R.id.sharecode_imagebutton_copy_teachers);

        sharecode_imagebutton_share_parents = findViewById(R.id.sharecode_imagebutton_share_parents);
        sharecode_imagebutton_share_teachers = findViewById(R.id.sharecode_imagebutton_share_teachers);

        code_Parents = sharecode_textview_parents.getText().toString();
        code_Teachers = sharecode_textview_teachers.getText().toString();

        // Function to DISPLAY codes on their TextView
        displayShareCodes();
        // Function to COPY codes on clipboard
        copyCodes();
        // Function to SHARE codes
        shareCodes();

        // Button to navigate to classrooms list
        Button button_to_classrooms = findViewById(R.id.button_to_classrooms);
        button_to_classrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate Classrooms List and close this one
                navClassroomsList();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        shareCodeRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null)
                {
                    return;
                }

                if(documentSnapshot.exists())
                {
                    String codeParent = documentSnapshot.getString("code_parent");
                    sharecode_textview_parents.setText(codeParent);

                    String codeTeacher = documentSnapshot.getString("code_teacher");
                    sharecode_textview_teachers.setText(codeTeacher);
                }
            }
        });
    }

    private void displayShareCodes() {
        shareCodeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                shareCodeRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            String codeParent = documentSnapshot.getString("code_parent");
                            sharecode_textview_parents.setText(codeParent);

                            String codeTeacher = documentSnapshot.getString("code_teacher");
                            sharecode_textview_teachers.setText(codeTeacher);
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage("No se encuentra en la base de datos");
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("No hay existencia del documento");
                    }
                });
    }

    private void copyCodes() {
        // Class to copy the parents' code
        sharecode_imagebutton_copy_parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard_parents = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData_parents = ClipData.newPlainText(sharecode_textview_insertcode_parents, sharecode_textview_parents.getText().toString());
                clipboard_parents.setPrimaryClip(clipData_parents);
                Toast.makeText(ShareCodeActivity.this, getString(R.string.toast_clipboard), Toast.LENGTH_SHORT).show();
            }
        });

        // Class to copy the teachers' code
        sharecode_imagebutton_copy_teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard_teachers = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData_teachers = ClipData.newPlainText(sharecode_textview_insertcode_teachers, sharecode_textview_teachers.getText().toString());
                clipboard_teachers.setPrimaryClip(clipData_teachers);
                Toast.makeText(ShareCodeActivity.this, getString(R.string.toast_clipboard), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareCodes() {
        // Share button for parents
        sharecode_imagebutton_share_parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeShared_parents = sharecode_textview_parents.getText().toString();

                Intent share_button = new Intent(Intent.ACTION_SEND);
                share_button.setType("text/plain");
                String shareBody = "*APP PADRES Y MAESTROS*\nEste es tu codigo para ingresar al aula como _padre_: \n"
                        + codeShared_parents;

                String shareSub = "Codigo para ingresar a un aula";
                share_button.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                share_button.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(share_button, "Share using"));
            }
        });

        // Share button for teachers
        sharecode_imagebutton_share_teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeShared_teachers = sharecode_textview_teachers.getText().toString();

                Intent share_button_teachers = new Intent(Intent.ACTION_SEND);
                share_button_teachers.setType("text/plain");
                // We should change this to call a XML string, for this time it works
                String shareBodys = "*PADRES Y MAESTROS*\nEste es tu codigo para ingresar al aula como _maestro_: \n"
                        + codeShared_teachers;

                String shareSubs = "Codigo para ingresar a un aula";
                share_button_teachers.putExtra(Intent.EXTRA_SUBJECT, shareSubs);
                share_button_teachers.putExtra(Intent.EXTRA_TEXT, shareBodys);
                startActivity(Intent.createChooser(share_button_teachers, "Share using"));
            }
        });
    }

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    // Navigate to Classrooms List and close this one
    private void navClassroomsList(){
        Intent classroomsList = new Intent(ShareCodeActivity.this, ClassroomsListActivity.class);
        startActivity(classroomsList);
        finish();
    }

    // Function to set Toast messages more easily
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}