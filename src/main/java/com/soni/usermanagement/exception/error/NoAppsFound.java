package com.soni.usermanagement.exception.error;

public class NoAppsFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoAppsFound() {
        super("No Applications found!");
    }
}