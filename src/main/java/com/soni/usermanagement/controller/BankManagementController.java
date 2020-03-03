package com.soni.usermanagement.controller;

import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;

import com.soni.usermanagement.repository.BankManagementRepo;
import com.soni.usermanagement.exception.error.BankAlreadyExists;
import com.soni.usermanagement.exception.error.BankNotFound;
import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.success.BankDeleted;
import com.soni.usermanagement.exception.success.BankUpdated;
import com.soni.usermanagement.exception.success.NewBankAdded;
import com.soni.usermanagement.model.BankManagement;
import com.soni.usermanagement.model.EmailValidation;

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
public class BankManagementController {

    @Autowired
    private BankManagementRepo repo;

    @GetMapping("/bank")
    public List<BankManagement> getAllBanks() {
        return repo.findAll();
    }

    @GetMapping("/bank/{bankCode}")
    public BankManagement getBank(@PathVariable("bankCode") String bankCode) {
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new BankNotFound(bankCode);
        else return bank;
    }

    @PostMapping("/bank")
    public void addBank(@RequestBody BankManagement newBank) {

        // checking for invalid e-mails
        List<String> contacts = Arrays.asList(newBank.getContacts().split(";[ ]*"));
        for(String contact: contacts)
        if(!EmailValidation.emailValidator(contact)) throw new EmailNotValidException(contact);

        // checking if entry already exists
        BankManagement bank = repo.findByBankCode(newBank.getBankCode()).orElse(null);
        if(bank != null) throw new BankAlreadyExists(bank.getBankCode(), bank.getBankName());

        repo.save(newBank);
        throw new NewBankAdded(newBank.getBankCode(), newBank.getBankName());
    }

    @DeleteMapping("/bank/{bankCode}")
    public void deleteBank(@PathVariable("bankCode") String bankCode) {
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new BankNotFound(bankCode);
        repo.deleteById(bank.getId());
        throw new BankDeleted(bank.getBankCode(), bank.getBankName());
    }

    @PutMapping("/bank/{bankCode}")
    public void updateBank(@Valid @RequestBody BankManagement newBank, @PathVariable("bankCode") String bankCode) {
        
        BankManagement bank = repo.findByBankCode(bankCode).orElse(null);
        if(bank == null) throw new BankNotFound(bankCode);

        // checking for duplicate entry
        BankManagement obj = repo.findByBankCode(newBank.getBankCode()).orElse(null);
        if(obj != null && !obj.getBankCode().equals(bank.getBankCode()))
        throw new BankAlreadyExists(obj.getBankCode(), obj.getBankName());

        // checking for invalid emails
        List<String> emails = Arrays.asList(newBank.getContacts().split(";[ ]*"));
        for(String email: emails)
        if (!EmailValidation.emailValidator(email)) throw new EmailNotValidException(email);

        bank.setBankCode(newBank.getBankCode());
        bank.setBankName(newBank.getBankName());
        bank.setContacts(newBank.getContacts());

        repo.save(bank);
        throw new BankUpdated(bank.getBankCode(), bank.getBankName());
    }

}