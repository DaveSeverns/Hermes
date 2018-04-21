package com.prestigeww.hermes.Activities;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.prestigeww.hermes.R;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;


public class ChatWindowActivity extends AppCompatActivity {

    ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatView = (ChatView) findViewById(R.id.chat_view);

        chatView.addMessage(new ChatMessage("Message received", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));

        //return true if successful and will add to chat ui
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                return true;
            }
        });

    }
}
