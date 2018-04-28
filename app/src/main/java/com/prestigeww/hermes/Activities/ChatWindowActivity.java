package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prestigeww.hermes.Adapters.MessageListAdapter;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.HermesConstants;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import com.prestigeww.hermes.Utilities.MessageViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

import static android.nfc.NdefRecord.createMime;


public class ChatWindowActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    ChatView chatView;
    NfcAdapter mNfcAdapter;
    String CID,UID;
    private DatabaseReference mDatabaseRef;
    private FirebaseProxy firebaseProxy;
    boolean adminbool=false;
    private DatabaseReference mChatThreadRef;
    List<MessageInChat> messagesList = new ArrayList<>();
    private MessageListAdapter messageListAdapter;
    private RecyclerView messageRecycler;

    Button sendButton;
    EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        sendButton = (Button) findViewById(R.id.sendButton);
        messageEditText = (EditText) findViewById(R.id.editTextSendMessage);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        messageListAdapter = new MessageListAdapter(messagesList);
        messageRecycler = findViewById(R.id.message_recycler);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));

        firebaseProxy = new FirebaseProxy(this);
        mChatThreadRef = firebaseProxy.mDatabaseReference.child(HermesConstants.THREAD_TABLE);

        CID=getIntent().getStringExtra("chat_id");
        String chatName = getIntent().getStringExtra("chatName");
        setTitle(chatName);
        UID=getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID",null);

        Query query = mChatThreadRef.child(CID).child("messages");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot messages:
                        dataSnapshot.getChildren()){
                    MessageInChat tempMessage = new MessageInChat(messages.child("body").getValue().toString(),
                            messages.child("sender").getValue().toString());


                    if(messagesList.contains(tempMessage)){
                        return;
                    }

                    messagesList.add(tempMessage);
                    messageListAdapter.notifyDataSetChanged();
                    messageRecycler.scrollToPosition(messagesList.size() -1);
                    Log.e("Value ", messages.child("body").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                if(email == null){
                    email = "defaultUser";
                }

                Log.d("sender", sender);

                mChatThreadRef = firebaseProxy.mDatabaseReference.child(HermesConstants.THREAD_TABLE).child(CID).child(HermesConstants.MESSAGES_TABLE);
                MessageInChat messageInChat = new MessageInChat(messageEditText.getText().toString(), email);
                mChatThreadRef.child("" + System.currentTimeMillis()).setValue(messageInChat);

                messagesList.clear();
                //messagesList.add(messageInChat);
                //messageListAdapter.notifyDataSetChanged();

            }
        });

        mNfcAdapter.setNdefPushMessageCallback(this, this);

        messageRecycler.setAdapter(messageListAdapter);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String cID=getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID",null);
        Toast.makeText(ChatWindowActivity.this,"chatID for NDEF:"+ cID,Toast.LENGTH_LONG).show();
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime("application/edu.temple.hermes",cID.getBytes())}
        );
        return msg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_window_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String utype = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserType", null);
        switch (item.getItemId()) {
            case R.id.deletechat:
                boolean admin=isAdmin();
                if (admin==true) {
                    Log.e("IsADMIN  check","retrun true");
                    if(firebaseProxy.isInternetAvailable(ChatWindowActivity.this)) {
                        firebaseProxy.deleteChatThread(CID);
                        new LocalDbHelper(ChatWindowActivity.this).deleteChatmember(CID);
                        Intent i = new Intent(ChatWindowActivity.this, ChatThreadFeedActivity.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(ChatWindowActivity.this, "Only Admin Cann Delete Chat", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.exitchat:
                String userid= ChatWindowActivity.this.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .getString("UserID",null);
                int del=new LocalDbHelper((ChatWindowActivity.this)).deleteChatmember(CID);
                Log.e("Deleted chat local",""+del);
                ArrayList<String> ids=new LocalDbHelper(ChatWindowActivity.this).getAllChatmember();
                new FirebaseProxy(this).postChatIDInUserToFirebase(ids, userid);
                Intent i = new Intent(ChatWindowActivity.this, ChatThreadFeedActivity.class);
                startActivity(i);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isAdmin(){
        Query q = firebaseProxy.mDatabaseReference.child("ChatThreads").child(CID).child("admin").equalTo(UID);
        Log.e("CID",CID);
        Log.e("UID",UID);
        Log.e("IsADMIN MEthod",q.toString());

        firebaseProxy.mDatabaseReference.child("ChatThreads").child(CID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    adminbool=false;
                   if(dataSnapshot.child("admin").getValue().toString() != null) {
                       String admin = dataSnapshot.child("admin").getValue().toString();
                       if (admin.equals(UID))
                           adminbool = true;
                   }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FB error: ", databaseError.getDetails());
            }
        });
        return adminbool;

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        //firebaseRecyclerAdapter.notifyDataSetChanged();
    }
}
