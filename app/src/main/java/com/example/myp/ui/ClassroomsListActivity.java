package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.myp.R;
import com.example.myp.adapters.ChatRoomsAdapter;
import com.example.myp.adapters.ClassroomsListRecyclerAdapter;
import com.example.myp.FireBase.models.Classroom;
import com.google.firebase.firestore.CollectionReference;
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
    private ArrayList<Classroom> mClassrooms = new ArrayList<>();
    private Set<String> mClassroomIds = new HashSet<>();

    // Declaration of the RecyclerView and it's properties (clicks & recycler adapter)
    private ClassroomsListRecyclerAdapter mClassroomsListRecyclerAdapter;
    private RecyclerView mClassroomsListRecyclerView;
    private ListenerRegistration mClassroomEventListener;
    private FirebaseFirestore mDb;

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

        CollectionReference classroomsCollection = mDb
                .collection(getString(R.string.collection_classrooms));

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
                    Intent intent = new Intent(ClassroomsListActivity.this, ChatRoomActivity.class);
                    //intent.putExtra(ChatRoomActivity.CHAT_ROOM_ID, chatRoom.getId());
                    //intent.putExtra(ChatRoomActivity.CHAT_ROOM_NAME, chatRoom.getName());
                    startActivity(intent);
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
        Intent chat = new Intent(ClassroomsListActivity.this, ChatRoomsAdapter.class);
        startActivity(chat);
        finish();
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

    // Function to initialize the XML objects as Java variables
    private void initViews(){
        // Java-RecyclerView linked to the XML-RecyclerView of all Classrooms
        mClassroomsListRecyclerView = findViewById(R.id.classrooms_recycler_view);

        // "Plus" floating action button
        findViewById(R.id.fab_create_classroom).setOnClickListener(this);
    }
}