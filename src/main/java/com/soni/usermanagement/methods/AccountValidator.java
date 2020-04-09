package com.soni.usermanagement.methods;

import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.model.AccountsModel;

public class AccountValidator {


    public static boolean validateAccount(AccountsModel newAccount) {

        String accountCode = newAccount.getAccountCode();
        String iban = newAccount.getIban();
        String entity = newAccount.getEntity();

        // checking lengths
        if(accountCode.length() > 6)
        throw new InvalidEntry(String.format("accountCode length should not be more than 6"));
        if(iban.length() > 6)
        throw new InvalidEntry(String.format("iban length should not be more than 6"));
        if(entity.length() > 6)
        throw new InvalidEntry(String.format("entity length should not be more than 6"));

        return true;
    }
}