package com.prestigeww.hermes.Utilities;

import android.util.Log;
import java.util.ArrayList;

import android.content.Context;

import android.content.Context;

public class NfcUtility extends HermesUtiltity {
    public NfcUtility(Context context){
        super(context);
    }
    public void addToChat(String chatID){
    }
    public void enterChat(String userID, ArrayList<String> ids){

        Log.e("Recived NFC Message",ids.get(ids.size()-1));
    }
}
