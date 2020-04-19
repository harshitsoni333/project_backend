package com.soni.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.dto.AccountsAppRequest;
import com.soni.usermanagement.dto.AccountsAppResponse;
import com.soni.usermanagement.dto.FileAppResponse;
import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.model.AccountsAppModel;
import com.soni.usermanagement.model.AccountsModel;
import com.soni.usermanagement.model.UserManagement;
import com.soni.usermanagement.repository.AccountsAppRepo;
import com.soni.usermanagement.repository.AccountsRepo;
import com.soni.usermanagement.repository.FileAppRepo;

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
public class AccountsAppController {

    @Autowired
    private AccountsAppRepo repo;
    @Autowired
    private FileAppRepo fileAppRepo;
    @Autowired
    private AccountsRepo accountsRepo;

    @GetMapping("/accountApps")
    public List<AccountsAppResponse> getAllAccountApps() {
        return repo.getInnerJoin();
    }

    @GetMapping("/accountApps/{keyword}")
    public AccountsAppResponse getAccountApp(@PathVariable("keyword") Long keyword) {

        List<AccountsAppResponse> accountApps = repo.getInnerJoin();        
        
        AccountsAppResponse accountApp = accountApps.stream()
            .filter(obj -> keyword.equals(obj.getID()))
            .findAny()
            .orElse(null);

        if(accountApp == null) throw new EntryNotFound(Long.toString(keyword));

        return accountApp;

    }

    @PostMapping("/accountApps")
    public ResponseEntity<?> addAccountApp(@RequestBody AccountsAppRequest newAccountApp) {
        
        // check if account id exists in accounts
        AccountsModel account = accountsRepo.findById(newAccountApp.getAccountID()).orElse(null);
        if(account == null)
        throw new EntryNotFound("Account ID does not exist in accounts table");


        // get all file-app-filetype combinations
        List<FileAppResponse> fileApps = fileAppRepo.getInnerJoinData();
        // find file-app-filetype combo
        FileAppResponse fileApp = fileApps.stream()
            .filter(obj -> obj.getFileCode().equals(newAccountApp.getFileCode())
                        && obj.getAppCode().equals(newAccountApp.getApplicationCode())
                        && obj.getFileTypeCode().equals(newAccountApp.getFileTypeCode()))
            .findAny()
            .orElse(null);
        
        if(fileApp == null)
        throw new EntryNotFound("file-app-filetype combination is not valid.");
        

        // account and file-app-filetype combo should be unique
        AccountsAppModel demoAccountApp = repo.findByAccountFileAppCombo(
            account.getId(), fileApp.getId()).orElse(null);

        // throw error accountApp already exists
        if(demoAccountApp != null) 
        throw new EntryAlreadyExists(
            "Account-App combination already exists with ID: ",
            Long.toString(demoAccountApp.getId()));

        // check for application count for one account
        if(repo.findApplicationCount(newAccountApp.getAccountID()) >= 5)
        throw new InvalidEntry("Account ID cannot have more than 5 application relations");

        // create new account-app entry
        AccountsAppModel accountsAppModel = new AccountsAppModel();
        accountsAppModel.setAccountID(account.getId());
        accountsAppModel.setFileAppID(fileApp.getId());
        if(UserManagement.getLastUpdatedDate() == null) {
            accountsAppModel.setLastUpdatedUserEmail(repo.findLastUpdatedDetails().getLastUpdatedUserEmail());
            accountsAppModel.setLastUpdatedDate(repo.findLastUpdatedDetails().getLastUpdatedDate());
        }
        else {
            accountsAppModel.setLastUpdatedUserEmail(UserManagement.getLastUpdatedUserEmail());
            accountsAppModel.setLastUpdatedDate(UserManagement.getLastUpdatedDate());
        }

        // save the new entry
        repo.save(accountsAppModel);

        return ResponseEntity.ok(new ResponseMessage(
            "New Account-App relation added"));
    }

    @DeleteMapping("/accountApps/{id}")
    public ResponseEntity<?> deleteAccountApp(@PathVariable("id") Long id) {

        // find by account-app id
        AccountsAppModel accountApp = repo.findById(id).orElse(null);
        if (accountApp == null)
        throw new EntryNotFound(Long.toString(id));
        
        // delete account
        repo.deleteById(id);

        return ResponseEntity.ok(new ResponseMessage(
            "AccountApp deleted: " + Long.toString(id)));
    }

    @PutMapping("/accountApps/{id}")
    public ResponseEntity<?> updateAccountApp(@Valid @RequestBody AccountsAppRequest newAccountApp, @PathVariable("id") Long id) {
        
        // find by account-app id
        AccountsAppModel accountApp = repo.findById(id).orElse(null);
        if (accountApp == null)
        throw new EntryNotFound(Long.toString(id));

        // check if account id exists in accounts
        AccountsModel account = accountsRepo.findById(newAccountApp.getAccountID()).orElse(null);
        if(account == null)
        throw new EntryNotFound("Account ID does not exist in accounts table");

        // get all file-app-filetype combinations
        List<FileAppResponse> fileApps = fileAppRepo.getInnerJoinData();
        // find file-app-filetype combo
        FileAppResponse fileApp = fileApps.stream()
            .filter(obj -> obj.getFileCode().equals(newAccountApp.getFileCode())
                        && obj.getAppCode().equals(newAccountApp.getApplicationCode())
                        && obj.getFileTypeCode().equals(newAccountApp.getFileTypeCode()))
            .findAny()
            .orElse(null);

        if(fileApp == null)
        throw new EntryNotFound("file-app-filetype combination is not valid.");

        // account and file-app-filetype combo should not exist already
        AccountsAppModel demoAccountApp = repo.findByAccountFileAppCombo(
            account.getId(), fileApp.getId()).orElse(null);

        // throw error accountApp already exists
        if(demoAccountApp != null && !demoAccountApp.getId().equals(accountApp.getId())) 
        throw new EntryAlreadyExists(
            "Account-App combination already exists with ID: ",
            Long.toString(demoAccountApp.getId()));
        
        // check for application count for one account
        if(newAccountApp.getAccountID() != accountApp.getAccountID()) {
            if(repo.findApplicationCount(newAccountApp.getAccountID()) >= 5)
            throw new InvalidEntry("Account ID cannot have more than 5 application relations");
        }

        // update the account-app entry
        accountApp.setAccountID(account.getId());
        accountApp.setFileAppID(fileApp.getId());
        if(UserManagement.getLastUpdatedDate() == null) {
            accountApp.setLastUpdatedUserEmail(repo.findLastUpdatedDetails().getLastUpdatedUserEmail());
            accountApp.setLastUpdatedDate(repo.findLastUpdatedDetails().getLastUpdatedDate());
        }
        else {
            accountApp.setLastUpdatedUserEmail(UserManagement.getLastUpdatedUserEmail());
            accountApp.setLastUpdatedDate(UserManagement.getLastUpdatedDate());
        }

        // save the new entry
        repo.save(accountApp);

        return ResponseEntity.ok(new ResponseMessage(
            "AccountApp updated: " + Long.toString(id)));
    }

}