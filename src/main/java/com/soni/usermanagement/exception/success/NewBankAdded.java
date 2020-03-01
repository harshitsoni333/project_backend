package com.soni.usermanagement.exception.success;

public class NewBankAdded extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NewBankAdded(String bankCode, String bankName) {
        super("New bank entry added: "+bankCode+" ("+bankName+")");
    }
}