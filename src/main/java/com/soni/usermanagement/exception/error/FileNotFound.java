package com.soni.usermanagement.exception.error;

public class FileNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileNotFound(String filecode) {
        super("File does not exists: " + filecode);
    }
}