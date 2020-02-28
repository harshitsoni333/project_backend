package com.soni.usermanagement.exception.success;

public class FileUpdated extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileUpdated(String filecode, String filename) {
        super("File updated: "+filecode+" ("+filename+")");
    }
}