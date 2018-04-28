package com.prestigeww.hermes.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ChatThread implements Serializable {

    private String chatId;
    private ArrayList<String> userIds;
    private String admin;
    private String chatName;
    private HashMap<String,MessageInChat> messages;

    public ChatThread(){
        chatId = "default";
        userIds = new ArrayList<String>();
        admin = null;
        chatName = "Hermes Chat";
        messages = new HashMap<>();
    }

    public ChatThread(String chatId, String admin, String chatName){
        this.chatId = chatId;
        this.admin = admin;
        this.chatName = chatName;
        userIds = new ArrayList<>();
        messages = new HashMap<>();
    }

    public ChatThread(String chatId, String chatName ){
        this.userIds = new ArrayList<>();
        this.messages = new HashMap<>();
        this.chatName = chatName;
        this.chatId = chatId;
        this.admin = null;
    }

    public ChatThread(ChatThread value) {
        this.chatId = value.chatId;
        this.admin = value.admin;
        this.chatName = value.chatName;
        this.messages = value.messages;
        this.userIds = value.userIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatThread)) return false;
        ChatThread that = (ChatThread) o;
        return Objects.equals(getChatId(), that.getChatId()) &&
                Objects.equals(getUserIds(), that.getUserIds()) &&
                Objects.equals(getAdmin(), that.getAdmin()) &&
                Objects.equals(getChatName(), that.getChatName()) &&
                Objects.equals(getMessages(), that.getMessages());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getChatId(), getUserIds(), getAdmin(), getChatName(), getMessages());
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public HashMap<String,MessageInChat> getMessages() {
        return messages;
    }

    public int addMessageToChatThread(String time,MessageInChat messageToAdd){
        messages.put(time, messageToAdd);
        return messages.size();
    }




    public void addUserId(String uid) {
        userIds.add(uid);
    }
}
