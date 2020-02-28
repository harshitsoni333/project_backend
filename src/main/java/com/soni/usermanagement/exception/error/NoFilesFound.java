package com.soni.usermanagement.exception.error;

public class NoFilesFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoFilesFound() {
        super("No files found!");
    }
}