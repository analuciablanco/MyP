package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.myp.R;
import com.example.myp.adapters.ClassroomsListRecyclerAdapter;
import com.example.myp.FireBase.models.Classroom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import android.view.Menu;
import android.view.MenuInflater;

import javax.annotation.Nullable;

// Clase que enlista todos los salones
public class ClassroomsListActivity extends AppCompatActivity implements
        View.OnClickListener,
        ClassroomsListRecyclerAdapter.ClassroomRecyclerClickListener {

    private static final String TAG = "ClassroomsListActivity";

    // Variables
    private ArrayList<Classroom> mClassrooms = new ArrayList<>();
    private Set<String> mClassroomIds = new HashSet<>();

    // Declaration of the RecyclerView and it's properties (clicks & recycler adapter)
    private ClassroomsListRecyclerAdapter mClassroomsListRecyclerAdapter;
    private RecyclerView mClassroomsListRecyclerView;
    private ListenerRegistration mClassroomEventListener;
    private FirebaseFirestore mDb;

    private ArrayList<Classroom> classrooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms_list);

        // Disable the back-button from the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Initialization of XML views as Java variables
        initViews();

        // FireBase FireStore Instance Initialization
        mDb = FirebaseFirestore.getInstance();

        // Recycler View Manipulation
        initClassroomsRecyclerView();
    }


    // Checks if the + action button has been tapped
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_create_classroom:{
                navHome();
            }
        }
    }

    // Initialization of RecyclerView for all classrooms
    private void initClassroomsRecyclerView(){
        mClassroomsListRecyclerAdapter = new ClassroomsListRecyclerAdapter(mClassrooms, this);
        mClassroomsListRecyclerView.setAdapter(mClassroomsListRecyclerAdapter);
        mClassroomsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Function to display the Classrooms stored on FireBase with the RecyclerView
    private void getClassrooms(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);
        CollectionReference classroomRef = mDb.collection(getString(R.string.collection_classrooms));
        /*classroomRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                      Classroom classroom = documentSnapshot.toObject(Classroom.class);
                     classrooms.add(classroom);
                    }
                }
            }
        });
        for(int i=0; i<=classrooms.size(); i++) {

            mDb.collection(getString(R.string.collection_classrom_members)).whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Classroom classroom = doc.toObject(Classroom.class);
                                    if (!mClassroomIds.contains(classroom.getID())) {
                                        mClassroomIds.add(classroom.getID());
                                        mClassrooms.add(classroom);
                                    }
                                }
                                Log.d(TAG, "onEvent: number of classrooms: " + mClassrooms.size());
                                mClassroomsListRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

    }*/


        mClassroomEventListener = classroomRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        if(!mClassroomIds.contains(classroom.getID())){
                            mClassroomIds.add(classroom.getID());
                            mClassrooms.add(classroom);
                        }
                    }
                    Log.d(TAG, "onEvent: number of classrooms: " + mClassrooms.size());
                    mClassroomsListRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Specific Navigation to the tapped chat list
    @Override
    public void onClassroomSelected(int position) {
        navChatListActivity(mClassrooms.get(position));
        // navChatListActivity( ((Classroom)(mClassrooms.toArray()[position])));
    }

    // Removes the classroom listener linked to FireBase once you close the app
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

    // Function to navigate to classroom creation (Home)
    public void navHome() {
        Intent home = new Intent(ClassroomsListActivity.this, HomeActivity.class);
        startActivity(home);
        finish();
    }

    // Navigate to the chat list screen once a classroom is tapped
    private void navChatListActivity(Classroom classroom){
        Intent chatList = new Intent(ClassroomsListActivity.this, ChatListActivity.class);
        chatList.putExtra(getString(R.string.intent_chatroom), classroom);
        startActivity(chatList);
    }

    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    // This function creates the delete button on the action bar and the sign out
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_button_classrooms,menu);
        return super.onCreateOptionsMenu(menu);

    }
    //This function validate and call when you press  the sign out item menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent mainActivity = new Intent(ClassroomsListActivity.this, MainActivity.class);
        //mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

    // Function to initialize the XML objects as Java variables
    private void initViews(){
        // Java-RecyclerView linked to the XML-RecyclerView of all Classrooms
        mClassroomsListRecyclerView = findViewById(R.id.classrooms_recycler_view);

        // "Plus" floating action button
        findViewById(R.id.fab_create_classroom).setOnClickListener(this);
    }
}