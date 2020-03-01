package com.soni.usermanagement.exception.success;

public class AppDeleted extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppDeleted(String appCode, String appName) {
        super("App entry deleted: "+appCode+" ("+appName+")");
    }
}