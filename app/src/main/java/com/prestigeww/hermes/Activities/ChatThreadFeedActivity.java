package com.prestigeww.hermes.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.prestigeww.hermes.Adapters.ThreadListAdapter;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
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
    private LocalDbHelper dbHelper;
    //private ThreadListAdapter threadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_thread_feed);
        firebaseProxy = new FirebaseProxy();

        recyclerView = findViewById(R.id.chat_recycler_view);
        dbHelper =  new LocalDbHelper(this);
        chatIds.addAll(dbHelper.getAllChatmember());


        //Log.e("Ids", );
        mDatabaseRef = firebaseProxy.mDatabaseReference.child("ChatThreads");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatThread,ThreadViewHolder>
                (ChatThread.class,
                        R.layout.thread_holder_view,
                        ThreadViewHolder.class,
                        mDatabaseRef) {
            @Override
            public void onBindViewHolder(final ThreadViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(viewHolder.getIdOfThread() != null){
                            Toast.makeText(ChatThreadFeedActivity.this, viewHolder.getIdOfThread(), Toast.LENGTH_SHORT).show();
                        }
                        Intent toChatWindow = new Intent(ChatThreadFeedActivity.this, ChatWindowActivity.class);
                        toChatWindow.putExtra("chat_id",viewHolder.getIdOfThread());
                        startActivity(toChatWindow);

                    }
                });
            }



            @Override
            protected void populateViewHolder(ThreadViewHolder viewHolder, ChatThread model, int position) {
                //if(!chatIds.contains(model.getChatId())){
                //    return;
                //}
                viewHolder.bindThread(model);
            }
        };
        //threadListAdapter = new ThreadListAdapter(chatThreads);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                String chatId;
                chatId = firebaseProxy.postThreadToFirebase(threadToAdd);
                dbHelper.insertChatmember(chatId);
                chatThreads.add(threadToAdd);
            }
        }).show();


    }
}
