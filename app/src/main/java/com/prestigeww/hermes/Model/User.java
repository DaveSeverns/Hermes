package com.prestigeww.hermes.Model;

import java.util.ArrayList;

public class User {
    public ArrayList<String> ChatID;
    public String username;
    public boolean isRegistered;

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public void setChatIds(ArrayList<String> chatIds) {
        this.ChatID = chatIds;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
