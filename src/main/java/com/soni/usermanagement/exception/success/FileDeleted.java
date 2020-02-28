package com.soni.usermanagement.exception.success;

public class FileDeleted extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileDeleted(String filecode, String filename) {
        super("File deleted: "+filecode+" ("+filename+")");
    }
}