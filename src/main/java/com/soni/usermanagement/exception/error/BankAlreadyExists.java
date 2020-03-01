package com.soni.usermanagement.exception.error;

public class BankAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BankAlreadyExists(String bankCode, String bankName) {
        super("Bank entry already exists: "+bankCode+" ("+bankName+")");
    }
}