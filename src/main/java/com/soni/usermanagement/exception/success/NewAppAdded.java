package com.soni.usermanagement.exception.success;

public class NewAppAdded extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NewAppAdded(String appCode, String appName) {
        super("New app entry added: "+appCode+" ("+appName+")");
    }
}