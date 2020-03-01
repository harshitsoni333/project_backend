package com.soni.usermanagement.exception.success;

public class BankUpdated extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BankUpdated(String bankCode, String bankName) {
        super("Bank entry updated: "+bankCode+" ("+bankName+")");
    }
}