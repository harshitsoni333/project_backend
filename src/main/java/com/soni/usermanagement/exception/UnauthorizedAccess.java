package com.soni.usermanagement.exception;

public class UnauthorizedAccess extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedAccess(String email) {
        super("Access denied for current user: " + email);
    }
}