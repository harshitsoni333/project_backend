package com.soni.usermanagement.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.soni.usermanagement.dto.AccountsAppRequest;
import com.soni.usermanagement.dto.AccountsAppResponse;
import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.exception.UnauthorizedAccess;
import com.soni.usermanagement.model.AccountsAppModel;
import com.soni.usermanagement.model.AccountsModel;
import com.soni.usermanagement.model.FileAppFileTypeModel;
import com.soni.usermanagement.model.UserManagement;
import com.soni.usermanagement.repository.AccountsAppRepo;
import com.soni.usermanagement.repository.AccountsRepo;
import com.soni.usermanagement.repository.FileAppModelRepo;
import com.soni.usermanagement.repository.FileAppRepo;
import com.soni.usermanagement.repository.UserManagementRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @Autowired
    private AccountsRepo accountsRepo;
    @Autowired
    private FileAppModelRepo fileAppModelRepo;
    @Autowired
    private UserManagementRepo userManagementRepo;

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
        
        // checking authorization
        UserManagement userManagement = userManagementRepo.findByEmail(principal.getName()).orElse(null);
        if(!userManagement.getProfile().equalsIgnoreCase("admin") && 
        !userManagement.getProfile().equalsIgnoreCase("update_only")) {
            throw new UnauthorizedAccess(principal.getName());
        }
        
        // get all file, app codes
        List<String> fileCodes = new ArrayList<>();
        List<String> appCodes = new ArrayList<>();

        if(newAccountApp.getApplication1() != ""){
        fileCodes.add(newAccountApp.getApplication1().substring(0, 3));
        appCodes.add(newAccountApp.getApplication1().substring(3));}

        if(newAccountApp.getApplication2() != ""){
        fileCodes.add(newAccountApp.getApplication2().substring(0, 3));
        appCodes.add(newAccountApp.getApplication2().substring(3));}

        if(newAccountApp.getApplication3() != ""){
        fileCodes.add(newAccountApp.getApplication3().substring(0, 3));
        appCodes.add(newAccountApp.getApplication3().substring(3));}

        if(newAccountApp.getApplication4() != ""){
        fileCodes.add(newAccountApp.getApplication4().substring(0, 3));
        appCodes.add(newAccountApp.getApplication4().substring(3));}
        
        if(newAccountApp.getApplication5() != ""){
        fileCodes.add(newAccountApp.getApplication5().substring(0, 3));
        appCodes.add(newAccountApp.getApplication5().substring(3));}

        // if no application specified
        if(fileCodes.size()<1 || appCodes.size()<1)
        throw new InvalidEntry("Specify at least one application");

        // check for duplicate entries in fields
        if(fileCodes.size()>1)
        for(int i=0; i<fileCodes.size()-1; i++) {
            for(int j=i+1; j<fileCodes.size(); j++) {
                if( fileCodes.get(i).equalsIgnoreCase(fileCodes.get(j)) && 
                    appCodes.get(i).equalsIgnoreCase(appCodes.get(j)))
                throw new InvalidEntry("Duplicate entries: " + fileCodes.get(i) + appCodes.get(j));
            }
        }

        // check if application exists in file-app and file-app-filetype table
        for(int i=0; i<fileCodes.size(); i++) {
            if(fileAppModelRepo.ifFileAppPresent(fileCodes.get(i), appCodes.get(i)) == 0L)
            throw new EntryNotFound("Application doesn't exist: " + 
                                    fileCodes.get(i) + appCodes.get(i));
            
            Long fileAppID = fileAppRepo.getFileAppID(fileCodes.get(i), appCodes.get(i));
            List<FileAppFileTypeModel> fileApps = fileAppRepo.findAll().stream()
                    .filter(obj -> obj.getFileAppID().equals(fileAppID))
                    .collect(Collectors.toList());
            if(fileApps.size()<1)
            throw new EntryNotFound("Application does not have any file-type: " +
                                    fileCodes.get(i) + appCodes.get(i));
        }

        // make an account object
        AccountsModel newAccount = new AccountsModel(
                newAccountApp.getAccountCode(),
                newAccountApp.getIban(),
                newAccountApp.getBankCode(),
                newAccountApp.getEntity());

        // save the account in accounts table
        if(repo.ifAccountExists(newAccountApp.getAccountCode()) == 0) {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<AccountsModel> request = new HttpEntity<>(newAccount, headers);
            RestTemplate restTemplate = new RestTemplate();
            AccountsModel account = restTemplate.postForObject(uri, request, AccountsModel.class);
        }

        // count applications associated with account
        Integer appsToBeAdded = fileCodes.size();
        Integer appsAlreadyPresent = repo.countApplications(newAccountApp.getAccountCode());
        System.out.println(appsAlreadyPresent);
        if((appsToBeAdded + appsAlreadyPresent) > 5)
        throw new InvalidEntry("Account cannot have more than 5 applications. " +
                                "Apps already present = " + Integer.toString(appsAlreadyPresent));
        
        // if application already exists for the given account
        for(int i=0; i<fileCodes.size(); i++) {
            if(repo.ifFileAppExists(newAccountApp.getAccountCode(), 
            fileCodes.get(i), 
            appCodes.get(i)) == 1L)
            throw new InvalidEntry("Application already exists for this account: "+
                        fileCodes.get(i) + appCodes.get(i));  
        }

        // save all combos in account-app table
        for(int i=0; i<fileCodes.size(); i++) {

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
                    fileApp.getFileTypeCode(),              // format
                    principal.getName(),                    // last updated user
                    LocalDate.now().toString() + 
                    " at " + LocalTime.now().toString()     // last updated date 
                ));
            }
        }

        return ResponseEntity.ok(new ResponseMessage(
            "New Account-App relation added"));
    }

    @DeleteMapping("/accountApps/{id}")
    public ResponseEntity<?> deleteAccountApp(@PathVariable("id") Long id,
    Principal principal) {

        // checking authorization
        UserManagement userManagement = userManagementRepo.findByEmail(principal.getName()).orElse(null);
        if(!userManagement.getProfile().equalsIgnoreCase("admin") && 
        !userManagement.getProfile().equalsIgnoreCase("update_only")) {
            throw new UnauthorizedAccess(principal.getName());
        }
        
        AccountsAppModel account = repo.findById(id).orElse(null);
        if(account == null)
        throw new EntryNotFound("ID = " + Long.toString(id));

        // delete account
        List<AccountsAppModel> accountApps = repo.findAllByAccountCode(account.getAccountCode());
        for(AccountsAppModel accountApp: accountApps) {
            repo.deleteById(accountApp.getId());
        }

        // delete in accounts table
        AccountsModel accountsModel = accountsRepo.findByAccountCode(account.getAccountCode()).orElse(null);
        accountsRepo.deleteById(accountsModel.getId());

        return ResponseEntity.ok(new ResponseMessage(
            "AccountApp deleted: " + Long.toString(id)));
    }

    @PutMapping("/accountApps/{accountCode}")
    public ResponseEntity<?> updateAccountApp(@Valid @RequestBody AccountsAppRequest newAccountApp, 
    @PathVariable("accountCode") String accountCode,
    Principal principal,
    @RequestHeader(name = "Authorization") String token) {
        
        // checking authorization
        UserManagement userManagement = userManagementRepo.findByEmail(principal.getName()).orElse(null);
        if(!userManagement.getProfile().equalsIgnoreCase("admin") && 
        !userManagement.getProfile().equalsIgnoreCase("update_only")) {
            throw new UnauthorizedAccess(principal.getName());
        }

        // get all file, app codes
        List<String> fileCodes = new ArrayList<>();
        List<String> appCodes = new ArrayList<>();

        if(newAccountApp.getApplication1() != ""){
        fileCodes.add(newAccountApp.getApplication1().substring(0, 3));
        appCodes.add(newAccountApp.getApplication1().substring(3));}

        if(newAccountApp.getApplication2() != ""){
        fileCodes.add(newAccountApp.getApplication2().substring(0, 3));
        appCodes.add(newAccountApp.getApplication2().substring(3));}

        if(newAccountApp.getApplication3() != ""){
        fileCodes.add(newAccountApp.getApplication3().substring(0, 3));
        appCodes.add(newAccountApp.getApplication3().substring(3));}

        if(newAccountApp.getApplication4() != ""){
        fileCodes.add(newAccountApp.getApplication4().substring(0, 3));
        appCodes.add(newAccountApp.getApplication4().substring(3));}
        
        if(newAccountApp.getApplication5() != ""){
        fileCodes.add(newAccountApp.getApplication5().substring(0, 3));
        appCodes.add(newAccountApp.getApplication5().substring(3));}
        

        // if no application specified
        if(fileCodes.size()<1 || appCodes.size()<1)
        throw new InvalidEntry("Specify at least one application");

        // check for duplicate entries in fields
        for(int i=0; i<fileCodes.size()-1; i++) {
            for(int j=i+1; j<fileCodes.size(); j++) {
                if( fileCodes.get(i).equalsIgnoreCase(fileCodes.get(j)) && 
                    appCodes.get(i).equalsIgnoreCase(appCodes.get(j)))
                throw new InvalidEntry("Duplicate entries: " + fileCodes.get(i) + appCodes.get(j));
            }
        }

        // check if application exists in file-app and file-app-filetype table
        for(int i=0; i<fileCodes.size(); i++) {
            if(fileAppModelRepo.ifFileAppPresent(fileCodes.get(i), appCodes.get(i)) == 0L)
            throw new EntryNotFound("Application doesn't exist: " + 
                                    fileCodes.get(i) + appCodes.get(i));
            
            Long fileAppID = fileAppRepo.getFileAppID(fileCodes.get(i), appCodes.get(i));
            List<FileAppFileTypeModel> fileApps = fileAppRepo.findAll().stream()
                    .filter(obj -> obj.getFileAppID().equals(fileAppID))
                    .collect(Collectors.toList());
            if(fileApps.size()<1)
            throw new EntryNotFound("Application does not have any file-type: " +
                                    fileCodes.get(i) + appCodes.get(i));
        }

        // make an account object
        AccountsModel newAccount = new AccountsModel(
                newAccountApp.getAccountCode(),
                newAccountApp.getIban(),
                newAccountApp.getBankCode(),
                newAccountApp.getEntity());
        
        // update the account in accounts table
        // try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<AccountsModel> request = new HttpEntity<>(newAccount, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(uri + "/" + newAccount.getAccountCode(), request);
        // } catch (Exception e) {
        //     throw new InvalidEntry(e.getLocalizedMessage());
        // }
        
        // delete old account-app entries
        List<AccountsAppModel> accountApps = repo.findAllByAccountCode(accountCode);
        for(AccountsAppModel accountApp: accountApps) {
            repo.deleteById(accountApp.getId());
        }

        // save all combos in account-app table
        for(int i=0; i<fileCodes.size(); i++) {

            // find id of file-app combo
            Long fileAppID = fileAppRepo.getFileAppID(fileCodes.get(i), appCodes.get(i));             

            // get all filetype combinations with given file-app
            List<FileAppFileTypeModel> fileApps = fileAppRepo.findAll().stream()
                    .filter(obj -> obj.getFileAppID().equals(fileAppID))
                    .collect(Collectors.toList());

            for(FileAppFileTypeModel fileApp: fileApps) {

                repo.save(new AccountsAppModel(
                    newAccountApp.getAccountCode(),               // account code
                    fileApp.getId(),                        // file-app ID
                    fileApp.getFileTypeCode(),              // format
                    principal.getName(),                    // last updated user
                    LocalDate.now().toString() + 
                    " at " + LocalTime.now().toString()     // last updated date 
                ));
            }
        }

        return ResponseEntity.ok(new ResponseMessage(
            "Account-App updated"));
    }

}