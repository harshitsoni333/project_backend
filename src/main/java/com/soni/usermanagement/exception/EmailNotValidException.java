package com.soni.usermanagement.exception;

public class EmailNotValidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailNotValidException(String email) {
        super("Email not valid: " + email);
    }
}