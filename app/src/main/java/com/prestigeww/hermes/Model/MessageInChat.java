package com.prestigeww.hermes.Model;

import android.os.Message;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MessageInChat implements Serializable{



    private String body;
    private String sender;

    private String docId;


    public MessageInChat(){

    }

    public MessageInChat(  String body, String sender) {

        this.body = body;
        this.sender = sender;
        this.docId =  "no_doc";
    }

    public MessageInChat(  String body, String sender, String docId){

        this.body = body;
        this.sender = sender;
        this.docId = docId;
    }

    private String getCurrentDateString(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
        Calendar dateObj = Calendar.getInstance();
        return dateFormat.format(dateObj);
    }



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }



    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
