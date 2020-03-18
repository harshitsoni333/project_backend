package com.soni.usermanagement.security;

import java.util.ArrayList;

public class User {
    private String userName;
    private String password;
    private ArrayList<String> authorities = new ArrayList<String>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(ArrayList<String> authorities) {
        this.authorities = authorities;
    }

    public User(String userName, String password, ArrayList<String> authorities) {
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
    }

    

}