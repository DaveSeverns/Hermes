package com.prestigeww.hermes.Utilities;

import android.util.Log;
import java.util.ArrayList;

public class NfcUtility extends HermesUtiltity {
    public void addToChat(String chatID){
    }
    public void enterChat(String userID, ArrayList<String> ids){
        new FirebaseProxy().postChatIDInUserToFirebase(ids,userID);
        Log.e("Recived NFC Message",ids.get(ids.size()-1));
    }
}
