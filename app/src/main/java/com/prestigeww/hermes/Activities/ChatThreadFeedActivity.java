package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.content.DialogInterface;
import android.os.Parcelable;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.prestigeww.hermes.Adapters.ThreadListAdapter;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import com.prestigeww.hermes.Utilities.ThreadViewHolder;
import java.util.ArrayList;
import java.util.Dictionary;


public class ChatThreadFeedActivity extends AppCompatActivity {

    private ArrayList<ChatThread> chatThreads = new ArrayList<>();
    private FirebaseProxy firebaseProxy;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private DatabaseReference mDatabaseRef;
    private ArrayList<String> chatIds = new ArrayList<>();
    private FloatingActionButton addChatButton;
    private LocalDbHelper dbHelper;
    private ThreadListAdapter threadListAdapter;
    ArrayList<ChatThread> tempChatThreads = new ArrayList<>();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    boolean[] contains;
    Query q;
    String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_thread_feed);
        firebaseProxy = new FirebaseProxy(this);
        recyclerView = findViewById(R.id.chat_recycler_view);
        dbHelper =  new LocalDbHelper(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = mFirebaseAuth ->{
            try {
                Log.e("Current User", mFirebaseAuth.getCurrentUser().getEmail());
            }catch (Exception e){
                e.printStackTrace();
            }

            if (mFirebaseAuth.getCurrentUser() == null){
                Intent backToLogin = new Intent(ChatThreadFeedActivity.this, LoginActivity.class);
                startActivity(backToLogin);
            }
        };
        //chatThreads = firebaseProxy.getChatsById(chatIds);
        //Log.e("Ids", );
        mDatabaseRef = firebaseProxy.mDatabaseReference.child("ChatThreads");
        chatThreads = firebaseProxy.getChatsById(chatIds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addChatButton = findViewById(R.id.add_new_chat_button);
        UID=getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID",null);
        addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChatAlert();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        //new GetListTask().execute();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatThread, ThreadViewHolder>(ChatThread.class,
                R.layout.thread_holder_view,
                ThreadViewHolder.class,
                mDatabaseRef) {
            @Override
            public void onBindViewHolder(final ThreadViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.getIdOfThread() != null) {
                            Toast.makeText(ChatThreadFeedActivity.this, viewHolder.getIdOfThread(), Toast.LENGTH_SHORT).show();
                            Intent toChatWindow = new Intent(ChatThreadFeedActivity.this, ChatWindowActivity.class);
                            toChatWindow.putExtra("chat_id", viewHolder.getIdOfThread());
                            startActivity(toChatWindow);
                        }


                    }
                });
            }

            @Override
            protected void populateViewHolder(ThreadViewHolder viewHolder, ChatThread model, int position) {
                viewHolder.bindThread(model);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
       // recyclerView.findViewHolderForItemId(2);

    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            String userid= ChatThreadFeedActivity.this.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getString("UserID",null);
            ArrayList<String> ids=new LocalDbHelper(ChatThreadFeedActivity.this).getAllChatmember();
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            // only one message sent during the beam
            NdefMessage msg = (NdefMessage) rawMsgs[0];//partnername

            // record 0 contains the MIME type, record 1 is the AAR, if present
            String chatID=new String(msg.getRecords()[0].getPayload());

            if(ids.contains(chatID)){
                Log.e("NFC recieved","already a member");
            }else {
                ids.add(chatID);
                new FirebaseProxy(this).postChatIDInUserToFirebase(ids, userid);
                new LocalDbHelper(ChatThreadFeedActivity.this).insertChatmember(chatID);
                Log.e("NFC recieved", chatID);
            }
        }
        //threadListAdapter.notifyDataSetChanged();
    }

    protected void addChatAlert(){

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_chat_dialog_box,null);
        final EditText addChatNameText = dialogView.findViewById(R.id.chat_name_edit_text);
        final EditText addMessageText = dialogView.findViewById(R.id.first_message_edit_text);
        new AlertDialog.Builder(this).setTitle("Add New Chat?")
                .setView(dialogView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChatThread threadToAdd = new ChatThread();
                MessageInChat message = new MessageInChat();
                threadToAdd.setChatName(addChatNameText.getText().toString());
                message.setBody(addMessageText.getText().toString());
                threadToAdd.addMessageToChatThread(message);
                threadToAdd.setAdmin(UID);
                String chatId;
                chatId = firebaseProxy.postThreadToFirebase(threadToAdd);
                dbHelper.insertChatmember(chatId);
                chatThreads.add(threadToAdd);
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String utype = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserType", null);
        switch (item.getItemId()) {
            case R.id.logout:
                mFirebaseAuth.signOut();
                new LocalDbHelper(ChatThreadFeedActivity.this).dropTables();
                finish();
                moveTaskToBack(true);
                // User chose the "Settings" item, show the app settings UI...
                return true;
            case R.id.registeraccount:

                if (!utype.equals("Registered")) {
                    Intent i = new Intent(ChatThreadFeedActivity.this, SIgnUpActivity.class);
                    i.putExtra("updateAccount", true);
                    startActivity(i);
                } else {
                    Toast.makeText(ChatThreadFeedActivity.this, "Already Registered", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.updateprofile:
                if(utype.equals("Registered")) {
                    Intent i = new Intent(ChatThreadFeedActivity.this, SIgnUpActivity.class);
                    i.putExtra("updateProfile", true);
                    startActivity(i);
                    Toast.makeText(ChatThreadFeedActivity.this,"will update profile",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ChatThreadFeedActivity.this,"Please Register Your Account",Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }
    }

    private class GetListTask extends AsyncTask<Void,Void,Void>{

        public GetListTask(){
            super();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            chatIds.addAll(dbHelper.getAllChatmember());

            if(!chatIds.isEmpty()){
                tempChatThreads.addAll(firebaseProxy.getChatsById(chatIds));
                if(tempChatThreads.isEmpty()){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chatThreads.addAll(tempChatThreads);
            threadListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);

    }


}

