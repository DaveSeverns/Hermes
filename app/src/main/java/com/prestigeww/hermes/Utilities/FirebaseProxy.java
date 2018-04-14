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
                    for (String id: chatIds){
                        for (DataSnapshot threadSnapshot: dataSnapshot.getChildren()){
                            if (threadSnapshot.getKey().equals("-"+id)){
                                String tempThreadName = (String) threadSnapshot.child("chatName").getValue();
                                String tempThreadId = (String) threadSnapshot.child("chatId").getValue();
                                Log.e("Temp Thread toString:", tempThreadName);
                                ChatThread tempThread = new ChatThread(tempThreadId,tempThreadName);
                                usersThreads.add(tempThread);
                            }
                        }
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


    public String postThreadToFirebase(ChatThread chatThread){
        String threadKey = mDatabaseReference.child("ChatThreads").push().getKey();
        mDatabaseReference.child(threadKey).setValue(chatThread);
        return threadKey;
    }


}
