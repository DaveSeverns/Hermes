package com.prestigeww.hermes.Model;

import android.os.Message;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MessageInChat implements Serializable{

    private String messageId;
    private String chatThreadId;
    private String body;
    private String sender;
    private String timeDateSent;
    private String docId;


    public MessageInChat(){

    }

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getChatThreadId() {
        return chatThreadId;
    }

    public void setChatThreadId(String chatThreadId) {
        this.chatThreadId = chatThreadId;
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

    public String getTimeDateSent() {
        return timeDateSent;
    }

    public void setTimeDateSent(String timeDateSent) {
        this.timeDateSent = timeDateSent;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
