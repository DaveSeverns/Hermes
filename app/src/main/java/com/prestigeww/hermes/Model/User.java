package com.prestigeww.hermes.Model;

import java.util.ArrayList;

public class User {
    public ArrayList<String> chatIds;
    public String username;
    public boolean isRegistered;

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public void setChatIds(ArrayList<String> chatIds) {
        this.chatIds = chatIds;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
