package com.soni.usermanagement.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;

import com.soni.usermanagement.repository.BankManagementRepo;
import com.soni.usermanagement.exception.error.BankAlreadyExists;
import com.soni.usermanagement.exception.error.BankNotFound;
import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.NoBanksFound;
import com.soni.usermanagement.exception.success.NewBankAdded;
import com.soni.usermanagement.model.BankManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BankManagementController {

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // E-Mail Validation function
    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean emailValidator(String email) {

        if (email == null) {
            return false;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    private BankManagementRepo repo;

    @GetMapping("/banks")
    public List<BankManagement> getAllBanks() {
        List<BankManagement> banks = repo.findAll();
        if(banks.isEmpty()) throw new NoBanksFound();
        else return banks;
    }

    @GetMapping("/banks/{bankCode}")
    public BankManagement getBank(@PathVariable("bankCode") String bankCode) {
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new BankNotFound(bankCode);
        else return bank;
    }

    @PostMapping("/banks")
    public void addBank(@RequestBody BankManagement newBank) {

        // checking for invalid e-mails
        List<String> contacts = Arrays.asList(newBank.getContacts().split(";[ ]*"));
        for(String contact: contacts) {
            if(!emailValidator(contact)) {
                // e-mail is not valid
                throw new EmailNotValidException(contact);
            }
        }

        // checking if entry already exists
        String bankCode = newBank.getBankCode();
        List<BankManagement> banks = repo.findAll();
        for(BankManagement bank: banks) {
            if(bank.getBankCode().equals(bankCode)) {
                throw new BankAlreadyExists(bank.getBankCode(), bank.getBankName());
            }
        }

        repo.save(newBank);
        throw new NewBankAdded(newBank.getBankCode(), newBank.getBankName());
    }


}