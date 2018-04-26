package com.prestigeww.hermes.Activities;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.prestigeww.hermes.R;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

import static android.nfc.NdefRecord.createMime;


public class ChatWindowActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    ChatView chatView;
    NfcAdapter mNfcAdapter;
    private String chatIdFromIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatIdFromIntent = getIntent().getStringExtra("chat_id");
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        chatView = (ChatView) findViewById(R.id.chat_view);
        chatView.addMessage(new ChatMessage("Message received", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
        //return true if successful and will add to chat ui
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                return true;
            }
        });

    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {


        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime("application/edu.temple.hermes",chatIdFromIntent.getBytes())}
        );
        return msg;
    }


}
