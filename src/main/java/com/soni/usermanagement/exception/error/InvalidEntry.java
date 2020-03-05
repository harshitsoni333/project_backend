package com.soni.usermanagement.exception.error;

public class InvalidEntry extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidEntry(String entry) {
        super("Entry is invalid: " + entry);
    }
}