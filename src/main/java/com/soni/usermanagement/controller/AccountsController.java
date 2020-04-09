package com.soni.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.methods.AccountValidator;
import com.soni.usermanagement.model.AccountsModel;
import com.soni.usermanagement.model.BankManagement;
import com.soni.usermanagement.model.ResponseMessage;
import com.soni.usermanagement.repository.AccountsRepo;
import com.soni.usermanagement.repository.BankManagementRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class AccountsController {

    @Autowired
    private AccountsRepo repo;
    @Autowired
    private BankManagementRepo bankRepo;

    @GetMapping("/accounts")
    public List<AccountsModel> getAllAccounts() {
        return repo.findAll();
    }

    @GetMapping("/accounts/{keyword}")
    public AccountsModel getAccount(@PathVariable("keyword") String keyword) {

        // find by account code
        AccountsModel account = repo.findByAccountCode(keyword).orElse(null);
        if (account == null) {
            // find by iban
            account = repo.findByIban(keyword).orElse(null);
        }
        if(account == null)
            throw new EntryNotFound(keyword);

        return account;
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> addAccount(@RequestBody AccountsModel newAccount) {

        if (!AccountValidator.validateAccount(newAccount))
            throw new InvalidEntry("Check the new contents again");

        // check for duplicate id
        AccountsModel account = repo.findById(newAccount.getId()).orElse(null);
        if(account != null) 
        throw new EntryAlreadyExists("duplicate ID = ", Long.toString(account.getId()));

        // check for duplicate accountCode
        account = repo.findByAccountCode(newAccount.getAccountCode()).orElse(null);
        if (account != null)
            throw new EntryAlreadyExists("duplicate accountCode = ", account.getAccountCode());

        // check for duplicate iban
        account = repo.findByIban(newAccount.getIban()).orElse(null);
        if (account != null)
            throw new EntryAlreadyExists("duplicate iban = ", account.getIban());

        // check if bank id exists
        BankManagement bank = bankRepo.findById(newAccount.getBankID()).orElse(null);
        if(bank == null)
            throw new EntryNotFound("bankID does not exist in banks = " + newAccount.getBankID().toString());

        // save new entry
        repo.save(newAccount);

        return ResponseEntity.ok(new ResponseMessage(
            "New account added: " + newAccount.getAccountCode()));
    }

    @DeleteMapping("/accounts/{keyword}")
    public ResponseEntity<?> deleteAccount(@PathVariable("keyword") String keyword) {

        // find by account code
        AccountsModel account = repo.findByAccountCode(keyword).orElse(null);
        if (account == null) {
            // find by iban
            account = repo.findByIban(keyword).orElse(null);
        }
        if(account == null)
            throw new EntryNotFound(keyword);
        
        // delete account
        repo.deleteById(account.getId());

        return ResponseEntity.ok(new ResponseMessage(
            "Account deleted: " + account.getAccountCode()));
    }

    @PutMapping("/accounts/{keyword}")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody AccountsModel newAccount, @PathVariable("keyword") String keyword) {
        
        // find by account code
        AccountsModel account = repo.findByAccountCode(keyword).orElse(null);
        if (account == null) {
            // find by iban
            account = repo.findByIban(keyword).orElse(null);
        }
        if(account == null)
            throw new EntryNotFound(keyword);

        // validate newAccount's constraints
        if (!AccountValidator.validateAccount(newAccount))
            throw new InvalidEntry("Check the new contents again");

        // checking for duplicate entry by accountCode
        AccountsModel obj = repo.findByAccountCode(newAccount.getAccountCode()).orElse(null);
        if(obj != null && !obj.getAccountCode().equals(account.getAccountCode()))
        throw new EntryAlreadyExists(obj.getAccountCode(), obj.getIban());

        // checking for duplicate iban
        obj = repo.findByIban(newAccount.getIban()).orElse(null);
        if(obj != null && !obj.getIban().equals(account.getIban()))
        throw new EntryAlreadyExists(obj.getAccountCode(), obj.getIban());

        // check if bank id exists
        BankManagement bank = bankRepo.findById(newAccount.getBankID()).orElse(null);
        if(bank == null)
        throw new EntryNotFound("bankID does not exist in banks = " + newAccount.getBankID().toString());

        account.setAccountCode(newAccount.getAccountCode());
        account.setIban(newAccount.getIban());
        account.setBankID(newAccount.getBankID());
        account.setEntity(newAccount.getEntity());

        // update account
        repo.save(account);

        return ResponseEntity.ok(new ResponseMessage(
            "Account updated: " + account.getAccountCode()));
    }
}