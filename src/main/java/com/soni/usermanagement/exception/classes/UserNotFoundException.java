package com.soni.usermanagement.exception.classes;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}