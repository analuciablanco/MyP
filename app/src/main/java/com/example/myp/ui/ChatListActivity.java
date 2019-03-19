package com.example.myp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myp.R;

// Clase de los chats de cada salón
public class ChatListActivity extends AppCompatActivity {

    // Declaration of Views as variables
    LinearLayout goToChat, goToChat2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Initialization of Java variables as XML Views
        initViews();

        // Function to navigate to a chat (without closing this screen)
        goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navChatActivity();
            }
        });
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
            startActivity(new Intent(this, ShareCodeActivity.class));
        }
        return false;
    }

    // Initialization of Java variables as XML Views
    private void initViews(){
        goToChat = findViewById(R.id.firstchat_layout);
        goToChat2 = findViewById(R.id.secondchat_layout);
    }

    // Navigate to the Individual Chat Screen
    private void navChatActivity(){
        Intent chat = new Intent(ChatListActivity.this, ChatActivity.class);
        startActivity(chat);
    }
}