package com.soni.usermanagement.exception;

public class EmailAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailAlreadyExists(String email) {
        super("Account already exists: " + email);
    }
}