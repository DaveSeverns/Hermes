package com.prestigeww.hermes.Model;

import java.util.ArrayList;
import java.util.Map;

public class RegisteredUser extends User {
    public String email;
    public RegisteredUser(String username, String email,  boolean isRegistered) {
        super();
        this.username=username;
        this.isRegistered=isRegistered;
        this.email=email;

    }
}
