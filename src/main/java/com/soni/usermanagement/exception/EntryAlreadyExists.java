package com.soni.usermanagement.exception;

public class EntryAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EntryAlreadyExists(String id, String name) {
        super("Entry already exists: " + id +" (" + name + ")");
    }
}