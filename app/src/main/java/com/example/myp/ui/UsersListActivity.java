package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myp.FireBase.models.User;
import com.example.myp.R;
import com.example.myp.adapters.ChatRoomsAdapter;
import com.example.myp.adapters.UsersListRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
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
public class UsersListActivity extends AppCompatActivity implements
        View.OnClickListener,
        UsersListRecyclerAdapter.UserRecyclerClickListener {

    private static final String TAG = "UsersListActivity";

    // Variables
    private ArrayList<User> mUsers = new ArrayList<>();
    private Set<String> mUserIds = new HashSet<>();

    // Declaration of the RecyclerView and it's properties (clicks & recycler adapter)
    private UsersListRecyclerAdapter mUsersListRecyclerAdapter;
    private RecyclerView mUsersListRecyclerView;
    private ListenerRegistration mUserEventListener;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        // Disable the back-button from the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Initialization of XML views as Java variables
        initViews();

        // FireBase FireStore Instance Initialization
        mDb = FirebaseFirestore.getInstance();

        // Recycler View Manipulation
        initUsersRecyclerView();
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
    private void initUsersRecyclerView(){
        mUsersListRecyclerAdapter = new UsersListRecyclerAdapter(mUsers, this);
        mUsersListRecyclerView.setAdapter(mUsersListRecyclerAdapter);
        mUsersListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Function to display the Classrooms stored on FireBase with the RecyclerView
    private void getUsers(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);

        CollectionReference usersCollection = mDb
                .collection(getString(R.string.collection_users));

        mUserEventListener = usersCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: called.");

                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if(queryDocumentSnapshots != null){
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        User user = doc.toObject(User.class);
                        if(!mUserIds.contains(user.getUserID())){
                            mUserIds.add(user.getUserID());
                            mUsers.add(user);
                        }
                    }
                    Log.d(TAG, "onEvent: number of users: " + mUsers.size());
                    mUsersListRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Specific Navigation to the tapped chat list
    @Override
    public void onUserSelected(int position) {
        // Intent intent = new Intent(UsersListActivity.this, ChatRoomActivity.class);
        Intent intent = new Intent(UsersListActivity.this, ChatRoomActivity.class);
        startActivity(intent);
    }

    // Removes the user listener linked to FireBase once you close the app
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUserEventListener != null){
            mUserEventListener.remove();
        }
    }

    // Checks Database once you resume the app
    @Override
    protected void onResume() {
        super.onResume();
        getUsers();
    }

    // Function to navigate to user creation (Home)
    public void navHome() {
        Intent home = new Intent(UsersListActivity.this, HomeActivity.class);
        startActivity(home);
        finish();
    }

    // Navigate to the chat list screen once a user is tapped
    private void navChatListActivity(User user){
        Intent chat = new Intent(UsersListActivity.this, ChatRoomsAdapter.class);
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
    //This function validate and call when you press  the sign out item in the menu.
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
        Intent mainActivity = new Intent(UsersListActivity.this, MainActivity.class);
        //mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

    // Function to initialize the XML objects as Java variables
    private void initViews(){
        // Java-RecyclerView linked to the XML-RecyclerView of all users
        mUsersListRecyclerView = findViewById(R.id.users_recycler_view);

        // "Plus" floating action button
        findViewById(R.id.fab_create_user).setOnClickListener(this);
    }
}