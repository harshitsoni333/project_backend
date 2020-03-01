package com.soni.usermanagement.exception.error;

public class AppAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppAlreadyExists(String appCode, String appName) {
        super("App entry already exists: "+appCode+" ("+appName+")");
    }
}