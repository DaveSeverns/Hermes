package com.prestigeww.hermes.Utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prestigeww.hermes.Model.ChatThread;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;


public class FirebaseProxy extends HermesUtiltity {
    public FirebaseApp firebaseApp;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;
    public static int size;

    public StorageReference mHermesStorage;
    public FirebaseProxy(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mHermesStorage = FirebaseStorage.getInstance().getReference();
    }



    public ArrayList<ChatThread> getChatsById(final ArrayList<String> chatIds){
        final ArrayList<ChatThread> usersThreads = new ArrayList<ChatThread>();
        size = chatIds.size();
        if (usersThreads.size() != size){
            for(String id : chatIds){
                mDatabaseReference.child("ChatThreads").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("chatName").getValue() != null){
                            String chatName = dataSnapshot.child("chatName").getValue().toString();
                            String chatId = dataSnapshot.child("chatId").getValue().toString();
                            ChatThread threadToAdd = new ChatThread(chatId,chatName);
                            usersThreads.add(threadToAdd);
                        }else {
                            size--;
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FB error: ", databaseError.getDetails());
                    }
                });
            }
        }

        return usersThreads;
    }


    public String postThreadToFirebase(ChatThread chatThread){
        String threadKey = mDatabaseReference.child("ChatThreads").push().getKey();
        chatThread.setChatId(threadKey);
        mDatabaseReference.child("ChatThreads").child(threadKey).setValue(chatThread);
        return threadKey;
    }


}
