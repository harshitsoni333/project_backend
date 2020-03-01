package com.soni.usermanagement.exception.success;

public class AppUpdated extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppUpdated(String appCode, String appName) {
        super("Bank entry updated: "+appCode+" ("+appName+")");
    }
}