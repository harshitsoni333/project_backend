package com.soni.usermanagement.exception.error;

public class BankNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BankNotFound(String bankCode) {
        super("Bank entry does not exist: "+bankCode);
    }
}