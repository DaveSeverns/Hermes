package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        firebaseProxy = new FirebaseProxy(this);

        //get ChatID and UserId
        CID=getIntent().getStringExtra("chat_id");
        UID=getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID",null);
        mNfcAdapter.setNdefPushMessageCallback(this, this);



    }


    //Messages




    //NFC
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

    //Menu
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
                if(utype.equals("Registered")) {
                    Intent i = new Intent(ChatWindowActivity.this, ChatThreadFeedActivity.class);
                    i.putExtra("updateProfile", true);
                    startActivity(i);
                    Toast.makeText(ChatWindowActivity.this,"will update profile",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ChatWindowActivity.this,"Please Register Your Account",Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }
    }

    //check if admin
    public boolean isAdmin(){
        Query q = firebaseProxy.mDatabaseReference.child("ChatThreads").child(CID).child("admin").equalTo(UID);
        Log.e("CID",CID);
        Log.e("UID",UID);
        Log.e("IsADMIN MEthod",q.toString());

        firebaseProxy.mDatabaseReference.child("ChatThreads").child(CID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

}
