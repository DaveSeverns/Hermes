package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.NfcUtility;
import com.prestigeww.hermes.Utilities.ThreadViewHolder;

import java.util.ArrayList;


public class ChatThreadFeedActivity extends AppCompatActivity {

    private ArrayList<ChatThread> chatThreads;
    private FirebaseProxy firebaseProxy;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private DatabaseReference mDatabaseRef;
    private ArrayList<String> chatIds = new ArrayList<>();
    private FloatingActionButton addChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_thread_feed);
        firebaseProxy = new FirebaseProxy();
        recyclerView = findViewById(R.id.chat_recycler_view);
        mDatabaseRef = firebaseProxy.mDatabaseReference.child("ChatThreads");
        chatThreads = firebaseProxy.getChatsById(chatIds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addChatButton = findViewById(R.id.add_new_chat_button);
        addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatThread,ThreadViewHolder>
                (ChatThread.class,
                        R.layout.thread_holder_view,
                        ThreadViewHolder.class,
                        mDatabaseRef) {
            @Override
            protected void populateViewHolder(ThreadViewHolder viewHolder, ChatThread model, int position) {
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
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            new NfcUtility().enterChat(getIntent());        }
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

}
