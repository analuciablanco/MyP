package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myp.R;
import com.example.myp.adapters.ClassroomsListRecyclerAdapter;
import com.example.myp.models.Classroom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

// Clase que enlista todos los salones
public class ClassroomsListActivity extends AppCompatActivity implements
        View.OnClickListener,
        ClassroomsListRecyclerAdapter.ClassroomRecyclerClickListener {
        private static final String TAG = "ClassroomsListActivity";

    // Variables
    private ArrayList<Classroom> mChatrooms = new ArrayList<>();
    private Set<String> mChatroomIds = new HashSet<>();

    private ClassroomsListRecyclerAdapter mClassroomsListRecyclerAdapter;
    private RecyclerView mClassroomsListRecyclerView;
    private ListenerRegistration mClassroomEventListener;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms_list);

        // Disable backButton from action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Button to add classrooms/head to home
        navigateHome();

        // Java-RecyclerView linked to the XML-RecyclerView of all Classrooms
        mClassroomsListRecyclerView = findViewById(R.id.classrooms_recycler_view);

        // "Plus" action button
        findViewById(R.id.fab_create_chatroom).setOnClickListener(this);

        // FireBase FireStore Instance Initialization
        mDb = FirebaseFirestore.getInstance();

        // Recycler View Manipulation
        initChatroomRecyclerView();
    }

    // Checks if the + action button has been tapped
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_create_chatroom:{newClassroomDialog();} // Cambia esto por la navegación a Home
        }
    }

    private void initChatroomRecyclerView(){
        mClassroomsListRecyclerAdapter = new ClassroomsListRecyclerAdapter(mChatrooms, this);
        mClassroomsListRecyclerView.setAdapter(mClassroomsListRecyclerAdapter);
        mClassroomsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getClassrooms(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);

        CollectionReference classroomsCollection = mDb
                .collection(getString(R.string.collection_chatrooms));

        mClassroomEventListener = classroomsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: called.");

                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if(queryDocumentSnapshots != null){
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Classroom classroom = doc.toObject(Classroom.class);
                        if(!mChatroomIds.contains(classroom.getClassroom_id())){
                            mChatroomIds.add(classroom.getClassroom_id());
                            mChatrooms.add(classroom);
                        }
                    }
                    Log.d(TAG, "onEvent: number of chatrooms: " + mChatrooms.size());
                    mClassroomsListRecyclerAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void buildNewClassroom(String classroomName){
        final Classroom classroom = new Classroom();
        classroom.setTitle(classroomName);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);

        DocumentReference newClassroomRef = mDb
                .collection(getString(R.string.collection_chatrooms))
                .document();

        classroom.setClassroom_id(newClassroomRef.getId());

        newClassroomRef.set(classroom).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    navChatListActivity(classroom);
                }else{
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Navigation to the chat list screen once a classroom is tapped
    private void navChatListActivity(Classroom classroom){
        Intent intent = new Intent(ClassroomsListActivity.this, ChatListActivity.class);
        intent.putExtra(getString(R.string.intent_chatroom), classroom);
        startActivity(intent);
    }

    // New classroom
    private void newClassroomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a classroom name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().equals("")){
                    buildNewClassroom(input.getText().toString());
                }
                else {
                    Toast.makeText(ClassroomsListActivity.this, "Enter a classroom name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Function for when a classroom is clicked
    @Override
    public void onClassroomSelected(int position) {
        navChatListActivity(mChatrooms.get(position));
        // navChatListActivity( ((Classroom)(mChatrooms.toArray()[position])));
    }

    // Checks database to remove deleted classrooms
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mClassroomEventListener != null){
            mClassroomEventListener.remove();
        }
    }

    // Checks Database once you resume the app
    @Override
    protected void onResume() {
        super.onResume();
        getClassrooms();
    }

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    // This function creates the delete button on the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_button_classrooms,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Function to navigate to classroom creation (Home)
    public void navigateHome() {
        FloatingActionButton floatingButton = findViewById(R.id.fab_create_chatroom);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHome = new Intent(ClassroomsListActivity.this, HomeActivity.class);
                startActivity(goToHome);
            }
        });
    }
}

