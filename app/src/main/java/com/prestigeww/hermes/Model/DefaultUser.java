package com.prestigeww.hermes.Model;

import java.util.ArrayList;

public class DefaultUser extends User {
    public DefaultUser(boolean isRegistered,String username){
        this.isRegistered=isRegistered;
        this.username=username;
        this.ChatID=new ArrayList<String>();
    }
    public DefaultUser(){

    }

}
