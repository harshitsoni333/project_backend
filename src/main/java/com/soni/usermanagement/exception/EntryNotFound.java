package com.soni.usermanagement.exception;

public class EntryNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EntryNotFound(String searchWord) {
        super("Entry does not exist: " + searchWord);
    }
}