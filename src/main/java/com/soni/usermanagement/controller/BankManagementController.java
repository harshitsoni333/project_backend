package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.model.BankManagement;
import com.soni.usermanagement.model.ResponseMessage;
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
public class BankManagementController {

    @Autowired
    private BankManagementRepo repo;

    @GetMapping("/banks")
    public List<BankManagement> getAllBanks() {
        return repo.findAll();
    }

    @GetMapping("/banks/{bankCode}")
    public BankManagement getBank(@PathVariable("bankCode") String bankCode) {
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new EntryNotFound(bankCode);
        else return bank;
    }

    @PostMapping("/banks")
    public ResponseEntity<?> addBank(@RequestBody BankManagement newBank) {

        // checking for invalid e-mails
        List<String> contacts = Arrays.asList(newBank.getContacts().split(";[ ]*"));
        for(String contact: contacts)
        if(!EmailValidation.emailValidator(contact)) throw new EmailNotValidException(contact);

        // checking if entry already exists
        BankManagement bank = repo.findByBankCode(newBank.getBankCode()).orElse(null);
        if(bank != null) throw new EntryAlreadyExists(bank.getBankCode(), bank.getBankName());

        repo.save(newBank);

        return ResponseEntity.ok(new ResponseMessage(
            "New bank added: " + newBank.getBankCode()));
    }

    @DeleteMapping("/banks/{bankCode}")
    public ResponseEntity<?> deleteBank(@PathVariable("bankCode") String bankCode) {

        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new EntryNotFound(bankCode);
        repo.deleteById(bank.getId());

        return ResponseEntity.ok(new ResponseMessage(
            "Bank deleted: " + bank.getBankCode()));
    }

    @PutMapping("/banks/{bankCode}")
    public ResponseEntity<?> updateBank(@Valid @RequestBody BankManagement newBank, @PathVariable("bankCode") String bankCode) {
        
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new EntryNotFound(bankCode);

        // checking for duplicate entry
        BankManagement obj = repo.findByBankCode(newBank.getBankCode()).orElse(null);
        if(obj != null && !obj.getBankCode().equals(bank.getBankCode()))
        throw new EntryAlreadyExists(obj.getBankCode(), obj.getBankName());

        // checking for invalid emails
        List<String> emails = Arrays.asList(newBank.getContacts().split(";[ ]*"));
        for(String email: emails)
        if (!EmailValidation.emailValidator(email)) throw new EmailNotValidException(email);

        bank.setBankCode(newBank.getBankCode());
        bank.setBankName(newBank.getBankName());
        bank.setContacts(newBank.getContacts());

        repo.save(bank);
        
        return ResponseEntity.ok(new ResponseMessage("Bank updated: " + bank.getBankCode()));
    }

}