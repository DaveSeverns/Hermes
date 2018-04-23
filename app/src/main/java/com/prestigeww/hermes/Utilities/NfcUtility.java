package com.prestigeww.hermes.Utilities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;

import android.content.Context;

public class NfcUtility extends HermesUtiltity {
    public NfcUtility(Context context){
        super(context);
    }
    public void addToChat(String chatID){
        /*
       */

    }
    public void enterChat(Intent intent){
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];//partnername

        // record 0 contains the MIME type, record 1 is the AAR, if present
        String chatID=new String(msg.getRecords()[0].getPayload());
        Log.e("Recived NFC Message",chatID);

    }
}
