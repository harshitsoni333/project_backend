package com.soni.usermanagement.exception.error;

public class FileAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileAlreadyExists(String filecode, String filename) {
        super("File already exists: "+filecode+" with Title: "+filename);
    }
}