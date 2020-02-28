package com.soni.usermanagement.exception.error;

public class NoUsersFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public NoUsersFoundException() {
        super("No users found!");
    }
}