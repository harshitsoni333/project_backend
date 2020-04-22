package com.soni.usermanagement.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.soni.usermanagement.dto.AccountsAppRequest;
import com.soni.usermanagement.dto.AccountsAppResponse;
import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.model.AccountsAppModel;
import com.soni.usermanagement.model.AccountsModel;
import com.soni.usermanagement.model.FileAppFileTypeModel;
import com.soni.usermanagement.repository.AccountsAppRepo;
import com.soni.usermanagement.repository.FileAppRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AccountsAppController {

    @Autowired
    private AccountsAppRepo repo;
    @Autowired
    private FileAppRepo fileAppRepo;

    final String uri = "http://localhost:8080/accounts";

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
    public ResponseEntity<?> addAccountApp(@RequestBody AccountsAppRequest newAccountApp, 
                                            Principal principal, 
                                            @RequestHeader(name = "Authorization") String token){
        
        // get all file, app codes
        List<String> fileCodes = new ArrayList<>();
        List<String> appCodes = new ArrayList<>();

        if(newAccountApp.getApplication1() != null)
        fileCodes.add(newAccountApp.getApplication1().substring(0, 3));
        appCodes.add(newAccountApp.getApplication1().substring(3));

        if(newAccountApp.getApplication2() != null)
        fileCodes.add(newAccountApp.getApplication2().substring(0, 3));
        appCodes.add(newAccountApp.getApplication2().substring(3));

        if(newAccountApp.getApplication3() != null)
        fileCodes.add(newAccountApp.getApplication3().substring(0, 3));
        appCodes.add(newAccountApp.getApplication3().substring(3));

        if(newAccountApp.getApplication4() != null)
        fileCodes.add(newAccountApp.getApplication4().substring(0, 3));
        appCodes.add(newAccountApp.getApplication4().substring(3));
        
        if(newAccountApp.getApplication5() != null)
        fileCodes.add(newAccountApp.getApplication5().substring(0, 3));
        appCodes.add(newAccountApp.getApplication5().substring(3));

        // if no application specified
        if(fileCodes.size()<1 || appCodes.size()<1)
        throw new InvalidEntry("Specify at least one application");

        // save the account in accounts table
        AccountsModel newAccount = new AccountsModel(
            newAccountApp.getAccountCode(),
            newAccountApp.getIban(),
            newAccountApp.getBankCode(),
            newAccountApp.getEntity()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<AccountsModel> request = new HttpEntity<>(newAccount, headers);
        RestTemplate restTemplate = new RestTemplate();
        AccountsModel account = restTemplate.postForObject(uri, request, AccountsModel.class);

        // save all combos in account-app table
        for(int i=0; i<5; i++) {

            // find id of file-app combo
            Long fileAppID = fileAppRepo.getFileAppID(fileCodes.get(i), appCodes.get(i));

            // get all filetype combinations with given file-app
            List<FileAppFileTypeModel> fileApps = fileAppRepo.findAll().stream()
                    .filter(obj -> obj.getFileAppID().equals(fileAppID))
                    .collect(Collectors.toList());

            for(FileAppFileTypeModel fileApp: fileApps) {

                repo.save(new AccountsAppModel(
                    newAccount.getAccountCode(),               // account code
                    fileApp.getId(),                        // file-app ID
                    newAccountApp.getFormat(),              // format
                    principal.getName(),                    // last updated user
                    LocalDate.now().toString() + 
                    " at " + LocalTime.now().toString()     // last updated date 
                ));
            }
        }

        return ResponseEntity.ok(new ResponseMessage(
            "New Account-App relation added"));
    }

    // @DeleteMapping("/accountApps/{id}")
    // public ResponseEntity<?> deleteAccountApp(@PathVariable("id") Long id) {

    //     // find by account-app id
    //     AccountsAppModel accountApp = repo.findById(id).orElse(null);
    //     if (accountApp == null)
    //     throw new EntryNotFound(Long.toString(id));
        
    //     // delete account
    //     repo.deleteById(id);

    //     return ResponseEntity.ok(new ResponseMessage(
    //         "AccountApp deleted: " + Long.toString(id)));
    // }

    // @PutMapping("/accountApps/{id}")
    // public ResponseEntity<?> updateAccountApp(@Valid @RequestBody AccountsAppRequest newAccountApp, @PathVariable("id") Long id) {
        
    //     // find by account-app id
    //     AccountsAppModel accountApp = repo.findById(id).orElse(null);
    //     if (accountApp == null)
    //     throw new EntryNotFound(Long.toString(id));

    //     // check if account id exists in accounts
    //     AccountsModel account = accountsRepo.findById(newAccountApp.getAccountID()).orElse(null);
    //     if(account == null)
    //     throw new EntryNotFound("Account ID does not exist in accounts table");

    //     // get all file-app-filetype combinations
    //     List<FileAppResponse> fileApps = fileAppRepo.getInnerJoinData();
    //     // find file-app-filetype combo
    //     FileAppResponse fileApp = fileApps.stream()
    //         .filter(obj -> obj.getFileCode().equals(newAccountApp.getFileCode())
    //                     && obj.getAppCode().equals(newAccountApp.getApplicationCode())
    //                     && obj.getFileTypeCode().equals(newAccountApp.getFileTypeCode()))
    //         .findAny()
    //         .orElse(null);

    //     if(fileApp == null)
    //     throw new EntryNotFound("file-app-filetype combination is not valid.");

    //     // account and file-app-filetype combo should not exist already
    //     AccountsAppModel demoAccountApp = repo.findByAccountFileAppCombo(
    //         account.getId(), fileApp.getId()).orElse(null);

    //     // throw error accountApp already exists
    //     if(demoAccountApp != null && !demoAccountApp.getId().equals(accountApp.getId())) 
    //     throw new EntryAlreadyExists(
    //         "Account-App combination already exists with ID: ",
    //         Long.toString(demoAccountApp.getId()));
        
    //     // check for application count for one account
    //     if(newAccountApp.getAccountID() != accountApp.getAccountID()) {
    //         if(repo.findApplicationCount(newAccountApp.getAccountID()) >= 5)
    //         throw new InvalidEntry("Account ID cannot have more than 5 application relations");
    //     }

    //     // update the account-app entry
    //     accountApp.setAccountID(account.getId());
    //     accountApp.setFileAppID(fileApp.getId());
    //     if(UserManagement.getLastUpdatedDate() == null) {
    //         accountApp.setLastUpdatedUserEmail(repo.findLastUpdatedDetails().getLastUpdatedUserEmail());
    //         accountApp.setLastUpdatedDate(repo.findLastUpdatedDetails().getLastUpdatedDate());
    //     }
    //     else {
    //         accountApp.setLastUpdatedUserEmail(UserManagement.getLastUpdatedUserEmail());
    //         accountApp.setLastUpdatedDate(UserManagement.getLastUpdatedDate());
    //     }

    //     // save the new entry
    //     repo.save(accountApp);

    //     return ResponseEntity.ok(new ResponseMessage(
    //         "AccountApp updated: " + Long.toString(id)));
    // }

}