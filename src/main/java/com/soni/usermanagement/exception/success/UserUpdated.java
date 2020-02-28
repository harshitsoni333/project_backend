package com.soni.usermanagement.exception.success;

public class UserUpdated extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserUpdated(String email) {
        super("User Updated: " + email);
    }
}