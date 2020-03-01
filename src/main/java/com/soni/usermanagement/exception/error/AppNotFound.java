package com.soni.usermanagement.exception.error;

public class AppNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppNotFound(String appCode) {
        super("App entry does not exist: "+appCode);
    }
}