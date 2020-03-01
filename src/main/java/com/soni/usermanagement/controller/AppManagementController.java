package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.AppNotFound;
import com.soni.usermanagement.exception.error.BankAlreadyExists;
import com.soni.usermanagement.exception.error.BankNotFound;
import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.NoAppsFound;
import com.soni.usermanagement.exception.success.BankDeleted;
import com.soni.usermanagement.exception.success.BankUpdated;
import com.soni.usermanagement.exception.success.NewBankAdded;
import com.soni.usermanagement.model.AppManagement;
import com.soni.usermanagement.model.BankManagement;
import com.soni.usermanagement.repository.AppManagementRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AppManagementController {

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
    private AppManagementRepo repo;

    @GetMapping("/apps")
    public List<AppManagement> getAllApps() {
        List<AppManagement> apps = repo.findAll();
        if(apps.isEmpty()) throw new NoAppsFound();
        else return apps;
    }

    @GetMapping("/apps/{appCode}")
    public AppManagement getApp(@PathVariable("appCode") String appCode) {
        AppManagement app = repo.findByAppCode(appCode).orElse(null);
        if(app == null) throw new AppNotFound(appCode);
        else return app;
    }
/*
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

    @DeleteMapping("/banks/{bankCode}")
    public void deleteBank(@PathVariable("bankCode") String bankCode) {
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new BankNotFound(bankCode);
        repo.deleteById(bank.getId());
        throw new BankDeleted(bank.getBankCode(), bank.getBankName());
    }

    @PutMapping("/banks/{bankCode}")
    public void updateBank(@Valid @RequestBody BankManagement newBank, @PathVariable("bankCode") String bankCode) {
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);

            if(bank == null) {
                throw new BankNotFound(bankCode);
            }

            // checking for duplicate entry
            List<BankManagement> banks = repo.findAll();
            for(BankManagement obj: banks) {
                if(obj.getBankCode().equals(bank.getBankCode())) continue;
                else if(obj.getBankCode().equals(newBank.getBankCode())) {
                        throw new BankAlreadyExists(obj.getBankCode(), obj.getBankName());
                }
            }

            // checking for invalid emails
            List<String> emails = Arrays.asList(newBank.getContacts().split(";[ ]*"));
            for(String email: emails) {
                // if email not valid
                if (!emailValidator(email)) {
                    throw new EmailNotValidException(email);
                }
            }

            bank.setBankCode(newBank.getBankCode());
            bank.setBankName(newBank.getBankName());
            bank.setContacts(newBank.getContacts());

            repo.save(bank);
            throw new BankUpdated(bank.getBankCode(), bank.getBankName());
    }

    */
}