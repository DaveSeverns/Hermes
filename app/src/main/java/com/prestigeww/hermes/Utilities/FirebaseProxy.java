package com.prestigeww.hermes.Utilities;

import android.content.Context;
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
    public static int size;


    public StorageReference mHermesStorage;
    public FirebaseProxy(Context context){
        super(context);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mHermesStorage = FirebaseStorage.getInstance().getReference();
    }



    public void getChatsById(final ArrayList<String> chatIds, FirebaseProxyInterface firebaseProxyInterface){
        final ArrayList<ChatThread> usersThreads = new ArrayList<ChatThread>();
        size = chatIds.size();
        if (usersThreads.size() != size){
            for(String id : chatIds){
                mDatabaseReference.child(HermesConstants.TEST_THREAD_TABLE).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("chatName").getValue() != null){
                            String chatName = dataSnapshot.child("chatName").getValue().toString();
                            String chatId = dataSnapshot.child("chatId").getValue().toString();
                            ChatThread threadToAdd = new ChatThread(chatId,chatName);
                            usersThreads.add(threadToAdd);
                            firebaseProxyInterface.getChatThread(threadToAdd);
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

        //return usersThreads;
    }



    public String postThreadToFirebase(ChatThread chatThread){
        String threadKey = mDatabaseReference.child(HermesConstants.TEST_THREAD_TABLE).push().getKey();
        chatThread.setChatId(threadKey);
        mDatabaseReference.child(HermesConstants.TEST_THREAD_TABLE).child(threadKey).setValue(chatThread);
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






    public interface FirebaseProxyInterface{
        public void getChatThread(ChatThread thread);
    }


}
