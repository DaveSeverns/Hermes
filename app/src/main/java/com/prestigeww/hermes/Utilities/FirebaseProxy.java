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


public class FirebaseProxy extends HermesUtiltity {
    public FirebaseApp firebaseApp;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;

    public StorageReference mHermesStorage;
    public FirebaseProxy(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mHermesStorage = FirebaseStorage.getInstance().getReference();
    }

    public ArrayList<ChatThread> getUsersChatsById(final ArrayList<String> chatIds){
        final ArrayList<ChatThread> usersThreads = new ArrayList<ChatThread>();

        while(usersThreads.isEmpty()){
            mDatabaseReference.child("ChatThreads").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (String id:chatIds){
                        ChatThread tempThread = (ChatThread)dataSnapshot.child(id).getValue();
                        usersThreads.add(tempThread);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Database Error: ", databaseError.getDetails());
                }
            });
        }


        return usersThreads;
    }


    public String addNewThreadToFirebase(ChatThread chatThread){
        String threadKey = mDatabaseReference.push().getKey();
        mDatabaseReference.child(threadKey).setValue(chatThread);
        return threadKey;
    }


}
