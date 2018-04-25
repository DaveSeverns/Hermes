package com.prestigeww.hermes.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public Map<String,String> chatIds;
    public String username;
    public boolean isRegistered;

    public User() {
        this.chatIds = new HashMap<>();
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public User(Map<String, String> chatIds, String username, boolean isRegistered) {
        this.chatIds = chatIds;
        this.username = username;
        this.isRegistered = isRegistered;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String> getChatIds() {
        return chatIds;
    }

    public void setChatIds(Map<String, String> chatIds) {
        this.chatIds = chatIds;
    }

    public void addId(String id, String name){
        chatIds.put(id, name);
    }
}
