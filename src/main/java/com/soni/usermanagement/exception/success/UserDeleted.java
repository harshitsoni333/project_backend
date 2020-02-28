package com.soni.usermanagement.exception.success;

public class UserDeleted extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserDeleted(String email) {
        super("User Deleted: " + email);
    }
}