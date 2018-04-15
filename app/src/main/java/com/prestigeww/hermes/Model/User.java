package com.prestigeww.hermes.Model;

public class User {
    private String username;
    private long id;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }//end setUsername

    public void setId(long id) {
        this.id = id;
    }//end setId

    public void setPassword(String password) {
        this.password = password;
    }// end setPassword

    public String getPassword() {
        return password;
    }//end getPassword

    public String getUsername() {
        return username;
    }//end getUsername

    public long getId() {
        return id;
    }//end getId
}//end USER
