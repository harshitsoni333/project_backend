package com.soni.usermanagement.exception.error;

public class UserAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExists(String email) {
        super("User with same email already exists: "+email);
    }
}