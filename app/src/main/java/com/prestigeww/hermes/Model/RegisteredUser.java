package com.prestigeww.hermes.Model;

import java.util.ArrayList;

public class RegisteredUser extends User {
    public String email;
    public String Password;
    public RegisteredUser(String username, String email, String password, boolean isRegistered) {
        this.username=username;
        this.isRegistered=isRegistered;
        this.Password=password;
        this.email=email;
        this.ChatID=new ArrayList<String>();
    }
}
