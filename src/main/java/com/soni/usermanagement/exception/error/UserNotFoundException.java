package com.soni.usermanagement.exception.error;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public UserNotFoundException(String email) {
        super("User does not exist: " + email);
    }
}