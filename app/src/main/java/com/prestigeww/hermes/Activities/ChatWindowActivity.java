package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;


public class ChatWindowActivity extends AppCompatActivity {

    ChatView chatView;
    private DatabaseReference mDatabaseReference;
    FirebaseProxy firebaseProxy = new FirebaseProxy();

    LocalDbHelper localDbHelper;

    MessageInChat messageInChat;
    List<MessageInChat> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatView = (ChatView) findViewById(R.id.chat_view);

        Intent intent = getIntent();
        String chatName = intent.getStringExtra("chatSelected");
        Toast.makeText(ChatWindowActivity.this, chatName, Toast.LENGTH_SHORT).show();
        Log.d("chatSelectedInWindow", chatName);

        chatView.addMessage(new ChatMessage("Message received", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));

        List<MessageInChat> testingFromFirebase = getMessagesFromFirebase();
        Log.d("TestingFromFirebase: ", testingFromFirebase.toString());


        //return true if successful and will add to chat ui
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {

                String chatId = "-LAaHzJNiSFz7n-il8mi";
                String chatName = "test";
                String messageId = "testMessage";
                String body = "This is a test message";
                String sender = "-LAQlcdVIByjfJ7zI2Hr";
                //ChatThread chatThread = new ChatThread(chatId, chatName);
                //messageInChat =  new MessageInChat(messageId, chatId, body, sender);
                //chatThread.addMessageToChatThread(messageInChat);

                //List<MessageInChat> testingFromFirebase = getMessagesFromFirebase();
                //Log.d("TestingFromFirebase: ", testingFromFirebase.toString());

               // messages = getMessagesFromFirebase();
                messageInChat = new MessageInChat();
                messageInChat.setBody(chatMessage.getMessage());

                messages.add(messageInChat);

                Log.d("messages list: ", messages.toString());

                mDatabaseReference = firebaseProxy.mDatabaseReference.child("ChatThreads");


                mDatabaseReference.child("-LAaHzJNiSFz7n-il8mi").child("messages").setValue(messages);


                return true;
            }
        });
    }

    public List<MessageInChat> getMessagesFromFirebase(){
        final GenericTypeIndicator<List<MessageInChat>> storedMessages = new GenericTypeIndicator<List<MessageInChat>>();
        //final List<MessageInChat>[] messages = {new ArrayList<>()};
        final List<String> mes = new ArrayList<>();


        DatabaseReference rootRef = firebaseProxy.mDatabaseReference.child("ChatThreads");
        DatabaseReference messagesRef = rootRef.child("-LAaHzJNiSFz7n-il8mi").child("messages");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    MessageInChat messages = new MessageInChat();
                    messages = ds.getValue(MessageInChat.class);
                    mes.add(messages.getBody());
                    Log.d("MessageFromFireBase", mes.toString());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        messagesRef.addListenerForSingleValueEvent(eventListener);

        return messages;
    }

}
