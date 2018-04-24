package com.prestigeww.hermes.Utilities;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.prestigeww.hermes.Activities.SIgnUpActivity;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.DefaultUser;
import com.prestigeww.hermes.Model.RegisteredUser;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


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



    public ArrayList<ChatThread> getChatsById(final ArrayList<String> chatIds){
        final ArrayList<ChatThread> usersThreads = new ArrayList<ChatThread>();
        while (usersThreads.size() != chatIds.size()){
            for(String id : chatIds){
                mDatabaseReference.child("ChatThreads").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String chatName = dataSnapshot.child("chatName").getValue().toString();
                        String chatId = dataSnapshot.child("chatId").getValue().toString();
                        ChatThread threadToAdd = new ChatThread(chatId,chatName);
                        usersThreads.add(threadToAdd);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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

    public String postDefaultUserToFirebase(){
        String threadKey = mDatabaseReference.child("User").push().getKey();
        mDatabaseReference.child("User").child(threadKey).setValue(new DefaultUser(false,threadKey));
        return threadKey;
    }
    public String postDefaultUserToFirebase(DefaultUser defaultUser){
        String threadKey = mDatabaseReference.child("User").push().getKey();
        mDatabaseReference.child("User").child(threadKey).setValue(defaultUser);
        return threadKey;
    }
    public String postRegisteredUserToFirebase(RegisteredUser registeredUser){
        String threadKey = mDatabaseReference.child("User").push().getKey();
        mDatabaseReference.child("User").child(threadKey).setValue(registeredUser);
        return threadKey;
    }
    public String postUserUpdateToFirebase(RegisteredUser registeredUser,String uid){
        mDatabaseReference.child("User").child(uid).setValue(registeredUser);
        return uid;
    }
    public void postChatIDInUserToFirebase(ArrayList<String> chatID,String UserID){
        String threadKey = mDatabaseReference.child("User").push().getKey();
       //String Ids= mDatabaseReference.child("User").child(UserID).child("ChatID").getKey();
        //new LocalDbHelper().getAllChatmember()
        Log.e("chat id that i got","jhjsgzfj");
        mDatabaseReference.child("User").child(UserID).child("ChatID").setValue(chatID);
    }
    public boolean isInternetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
