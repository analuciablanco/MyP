package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.myp.FireBase.models.ClassRoomMember;
import com.example.myp.R;
import com.example.myp.adapters.ChatListRecyclerAdapter;
import com.example.myp.adapters.ClassroomsListRecyclerAdapter;
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
import com.example.myp.R;

// Clase de los chats de cada salón
public class ChatListActivity extends AppCompatActivity implements
        View.OnClickListener,
        ChatListRecyclerAdapter.ChatListRecyclerClickListener {

    private static final String TAG = "ChatListActivity";

    // Variables
    private ArrayList<ClassRoomMember> mClassRoomMember = new ArrayList<>();
    private Set<String> mClassRoomMemberIds = new HashSet<>();

    // Declaration of the RecyclerView and it's properties (clicks & recycler adapter)
    private ChatListRecyclerAdapter mChatListRecyclerAdapter;
    private RecyclerView mChatListRecyclerView;
    private ListenerRegistration mChatListEventListener;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Initialization of Java variables as XML Views
        initViews();

        // FireBase FireStore Instance Initialization
        mDb = FirebaseFirestore.getInstance();

        // Recycler View Manipulation
        initChatListRecyclerView();
    }

    // Initialization of RecyclerView for all classroom members
    private void initChatListRecyclerView(){
        mChatListRecyclerAdapter = new ChatListRecyclerAdapter(mClassRoomMember, this);
        mChatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatListRecyclerView.setAdapter(mChatListRecyclerAdapter);
    }

    // Function to display the Classroom members with the RecyclerView
    private void getChatLists(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);

        CollectionReference
                classroomMembersRef = mDb.collection(getString(R.string.collection_classrooms))
                .document(classroomID)

        mChatListEventListener = chatListCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if(queryDocumentSnapshots != null){
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        ClassRoomMember classRoomMember = doc.toObject(ClassRoomMember.class);
                        if(!mClassRoomMemberIds.contains(classRoomMember.getUser_id())){
                            mClassRoomMemberIds.add(classRoomMember.getUser_id());
                            mClassRoomMember.add(classRoomMember);
                        }
                    }
                    Log.d(TAG, "onEvent: number of classrooms: " + mClassRoomMember.size());
                    mChatListRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Specific Navigation to the tapped chat list
    @Override
    public void onClassroomSelected(int position) {
        navChatActivity(mClassRoomMember.get(position));
        // navChatListActivity( ((Classroom)(mClassrooms.toArray()[position])));
    }

    // Removes the classroom listener linked to FireBase once you close the app
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChatListEventListener != null){
            mChatListEventListener.remove();
        }
    }

    // Checks Database once you resume the app
    @Override
    protected void onResume() {
        super.onResume();
        getChatLists();
    }


    // This class adds the delete button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_chatlist, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // ???
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_show_code) {
            Toast.makeText(this, "Show code", Toast.LENGTH_SHORT).show();
            Intent chat = new Intent(ChatListActivity.this, ShareCodeActivity.class);
            startActivity(chat);
        }
        return false;
    }

    // Initialization of Java variables as XML Views
    private void initViews(){
        // Java-RecyclerView linked to the XML-RecyclerView of all Classrooms
        mChatListRecyclerView = findViewById(R.id.chat_list_recycler_view);
    }

    // Navigate to the Individual Chat Screen
    private void navChatActivity(ClassRoomMember classRoomMember){
        Intent chat = new Intent(ChatListActivity.this, ChatActivity.class);
        chat.putExtra(getString(R.string.intent_user_list), classRoomMember);
        startActivity(chat);
    }

    @Override
    public void onClick(View view) {

    }
}