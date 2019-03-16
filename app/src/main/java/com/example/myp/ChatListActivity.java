package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

// Clase de los chats de cada salón
public class ChatListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Function to navigate to a chat
        LinearLayout goToChat = findViewById(R.id.firstchat_layout);
        goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatList = new Intent(ChatListActivity.this, ChatActivity.class);
                startActivity(chatList);
            }
        });

        // Function to navigate to a chat
        LinearLayout goToChat2 = findViewById(R.id.secondchat_layout);
        goToChat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatList2 = new Intent(ChatListActivity.this, ChatActivity.class);
                startActivity(chatList2);
            }
        });
    }
}