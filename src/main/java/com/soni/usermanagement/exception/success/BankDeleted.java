package com.soni.usermanagement.exception.success;

public class BankDeleted extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BankDeleted(String bankCode, String bankName) {
        super("Bank entry deleted: "+bankCode+" ("+bankName+")");
    }
}