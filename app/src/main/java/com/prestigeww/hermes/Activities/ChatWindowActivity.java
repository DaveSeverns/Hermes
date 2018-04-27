package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
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

import org.w3c.dom.Text;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;


public class ChatWindowActivity extends AppCompatActivity {

    ChatView chatView;
    private DatabaseReference mDatabaseReference;
    FirebaseProxy firebaseProxy = new FirebaseProxy();

    LocalDbHelper localDbHelper;
    Button sendbutton;
    ListView listViewOfMessages;
    EditText messageEditText;
    FirebaseListAdapter<MessageInChat> firebaseListAdapter;

    MessageInChat messageInChat;
    List<MessageInChat> messages = new ArrayList<>();
    List<MessageInChat> originalMessageList = new ArrayList<>();

    String chatId;
    List<String> messagesToAdd = new ArrayList<>();
    HashMap<String, Object> hashMap = new HashMap<>();
    int i = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listViewOfMessages = (ListView) findViewById(R.id.listViewOfMessages);
        sendbutton = (Button) findViewById(R.id.sendMessageButton);
        messageEditText = (EditText) findViewById(R.id.editTextToEnterMessage);

        Intent intent = getIntent();
        chatId = intent.getStringExtra("chat_id");
        Toast.makeText(ChatWindowActivity.this, chatId, Toast.LENGTH_SHORT).show();
        Log.d("chatSelectedInWindow", chatId);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrieve all the data from the database and update
                //set body of messageInChat object from editText and add to listView

                //generate UUID for message ID

                UUID uuid = UUID.randomUUID();
                String randomUUID = uuid.toString();
                messageInChat = new MessageInChat(randomUUID, chatId, messageEditText.getText().toString(), "sender");
                originalMessageList.add(messageInChat);

                //messages.add(messageInChat);
                //Log.d("messages list: ", messages.toString());

                // originalMessageList.addAll(messages);

                hashMap.put("messages", originalMessageList);

                //get to right position as needed to send and receive message updates
                mDatabaseReference = firebaseProxy.mDatabaseReference.child("ChatThreads");
                DatabaseReference chatThread = mDatabaseReference.child(chatId);
                chatThread.updateChildren(hashMap);

                // get rid of old message list contents so that when we populate views again, we don't have duplicates
                originalMessageList = new ArrayList<>();




                // get list of messages currently in "message list"
                // append MessageInChat objects to list of messages retrieved
                // update chat thread with entire list

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        mDatabaseReference = firebaseProxy.mDatabaseReference.child("ChatThreads");
        mDatabaseReference = mDatabaseReference.child(chatId).child("messages");

        //set firebaselistAdapter for changes
        firebaseListAdapter = new FirebaseListAdapter<MessageInChat>(this, MessageInChat.class, R.layout.textview_for_listview, mDatabaseReference) {

            @Override
            protected void populateView(View v, MessageInChat model, int position) {

                // this method gets called for every record in the chat messages list
                // it gets called again ASYNCHRONOUSLY when a new message is added from another device
                Log.e("Firebase", "Received model with message: "
                        + model.getBody() + ", and ID: " + model.getMessageId());

                TextView textView = (TextView)v.findViewById(R.id.textViewForChat);
                originalMessageList.add(model);
                textView.setText(model.getBody());
            }
        };
        listViewOfMessages.setAdapter(firebaseListAdapter);

    }

 /*   public List<MessageInChat> getMessagesFromFirebase(){
        final GenericTypeIndicator<List<MessageInChat>> storedMessages = new GenericTypeIndicator<List<MessageInChat>>();
        //final List<MessageInChat>[] messages = {new ArrayList<>()};
        final List<String> mes = new ArrayList<>();


        DatabaseReference rootRef = firebaseProxy.mDatabaseReference.child("ChatThreads");
        DatabaseReference messagesRef = rootRef.child(chatId).child("messages");

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
*/


}
