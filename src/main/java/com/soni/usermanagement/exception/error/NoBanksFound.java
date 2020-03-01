package com.soni.usermanagement.exception.error;

public class NoBanksFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoBanksFound() {
        super("No bank entries found!");
    }
}