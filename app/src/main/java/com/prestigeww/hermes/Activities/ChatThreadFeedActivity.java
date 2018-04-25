package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prestigeww.hermes.Adapters.ThreadListAdapter;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.HermesConstants;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import com.prestigeww.hermes.Utilities.NfcUtility;
import com.prestigeww.hermes.Utilities.ThreadViewHolder;

import java.util.ArrayList;
import java.util.Dictionary;

import static com.prestigeww.hermes.Utilities.HermesConstants.TEST_THREAD_TABLE;
import static com.prestigeww.hermes.Utilities.HermesConstants.THREAD_TABLE;


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
    private ArrayList<ChatThread> tempChatThreads = new ArrayList<>();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentAuthUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_thread_feed);
        firebaseProxy = new FirebaseProxy(this);
        dbHelper = new LocalDbHelper(this);
        chatIds = dbHelper.getAllChatmember();
        Log.e("Chat Ids?", chatIds.get(0).toString());
        recyclerView = findViewById(R.id.chat_recycler_view);
        dbHelper =  new LocalDbHelper(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentAuthUser = mFirebaseAuth.getCurrentUser();
        mAuthListener = mFirebaseAuth ->{
            try {
                Log.e("Current User", currentAuthUser.getEmail());
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
        mDatabaseRef = firebaseProxy.mDatabaseReference.child(TEST_THREAD_TABLE);
        chatThreads = firebaseProxy.getChatsById(chatIds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //threadListAdapter = new ThreadListAdapter(chatThreads);
        //recyclerView.setAdapter(threadListAdapter);

        addChatButton = findViewById(R.id.add_new_chat_button);
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
                //if(!chatIds.contains(model.getChatId())){
                    //return;
        //                }
                viewHolder.bindThread(model);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            new NfcUtility(this).enterChat(getIntent());        }
        firebaseRecyclerAdapter.notifyDataSetChanged();
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
                addChat(threadToAdd);
            }
        }).show();


    }

    public void addChat(ChatThread chatThread){
        String chatId;
        chatThread.addUserId(currentAuthUser.getUid());
        chatId = firebaseProxy.postThreadToFirebase(chatThread);

        FirebaseDatabase.getInstance().getReference().child(HermesConstants.TEST_USER_TABLE);
        dbHelper.insertChatmember(chatId);
        chatThreads.add(chatThread);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.log_out_btn){
            mFirebaseAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}
