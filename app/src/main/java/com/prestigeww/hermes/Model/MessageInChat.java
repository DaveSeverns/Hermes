package com.prestigeww.hermes.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MessageInChat {

    private String messageId;
    private String chatThreadId;
    private String body;
    private String sender;
    private String timeDateSent;
    private String docId;

    public MessageInChat(String messageId, String chatThreadId, String body, String sender) {
        this.messageId = messageId;
        this.chatThreadId = chatThreadId;
        this.body = body;
        this.sender = sender;
        this.timeDateSent = getCurrentDateString();
        this.docId =  null;
    }

    public MessageInChat(String messageId, String chatThreadId, String body, String sender, String docId){
        this.messageId = messageId;
        this.chatThreadId = chatThreadId;
        this.body = body;
        this.sender = sender;
        this.timeDateSent = getCurrentDateString();
        this.docId = docId;
    }

    private String getCurrentDateString(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
        Calendar dateObj = Calendar.getInstance();
        return dateFormat.format(dateObj);
    }
}
