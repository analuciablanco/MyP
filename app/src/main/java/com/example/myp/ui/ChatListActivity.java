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

    // This class adds the button delete
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_chatlist, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_show_code) {
            Toast.makeText(this, "Show code", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShareCodeActivity.class));
        }
        return false;
    }

}