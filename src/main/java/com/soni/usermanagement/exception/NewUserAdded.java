package com.soni.usermanagement.exception;

public class NewUserAdded extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NewUserAdded(String email) {
        super("New User Added: " + email);
    }
}