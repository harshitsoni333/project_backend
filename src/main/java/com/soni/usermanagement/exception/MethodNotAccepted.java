package com.soni.usermanagement.exception;

public class MethodNotAccepted extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MethodNotAccepted(String entry) {
        super("Method is not accepted: " + entry);
    }
}