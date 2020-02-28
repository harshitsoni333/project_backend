package com.soni.usermanagement.exception.success;

public class NewFileAdded extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NewFileAdded(String filecode, String filename) {
        super("New file added: "+filecode+" ("+filename+")");
    }
}