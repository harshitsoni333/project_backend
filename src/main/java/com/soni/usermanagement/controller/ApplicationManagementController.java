package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.EntryAlreadyExists;
import com.soni.usermanagement.exception.error.EntryNotFound;
import com.soni.usermanagement.exception.success.AppDeleted;
import com.soni.usermanagement.exception.success.AppUpdated;
import com.soni.usermanagement.exception.success.NewAppAdded;
import com.soni.usermanagement.model.ApplicationManagement;
import com.soni.usermanagement.model.EmailValidation;
import com.soni.usermanagement.repository.ApplicationManagementRepo;

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
public class ApplicationManagementController {

    @Autowired
    private ApplicationManagementRepo repo;

    @GetMapping("/application")
    public List<ApplicationManagement> getAllApplications() {
        return repo.findAll();
    }

    @GetMapping("/application/{appCode}")
    public ApplicationManagement getApplication(@PathVariable("appCode") String appCode) {
        ApplicationManagement application = repo.findByAppCode(appCode).orElse(null);
        if(application == null) throw new EntryNotFound(appCode);
        else return application;
    }

    @PostMapping("/application")
    public void addApplication(@RequestBody ApplicationManagement newApplication) {

        // checking for invalid e-mails
        List<String> contacts = Arrays.asList(newApplication.getContacts().split(";[ ]*"));
        if(!newApplication.getContacts().equals(""))
        for(String contact: contacts)
        if(!EmailValidation.emailValidator(contact)) throw new EmailNotValidException(contact);

        // checking if entry already exists
        ApplicationManagement application = repo.findByAppCode(newApplication.getAppCode()).orElse(null);
        if(application != null) throw new EntryAlreadyExists(application.getAppCode(), application.getAppName());

        repo.save(newApplication);
        throw new NewAppAdded(newApplication.getAppCode(), newApplication.getAppName());
    }

    @DeleteMapping("/application/{appCode}")
    public void deleteApplication(@PathVariable("appCode") String appCode) {
        ApplicationManagement application = repo.findByAppCode(appCode).orElse(null);
        if(application == null) throw new EntryNotFound(appCode);
        repo.deleteById(application.getId());
        throw new AppDeleted(application.getAppCode(), application.getAppName());
    }

    @PutMapping("/application/{appCode}")
    public void updateApplication(@Valid @RequestBody ApplicationManagement newApplication, @PathVariable("appCode") String appCode) {
        ApplicationManagement application = repo.findByAppCode(appCode).orElse(null);

        if(application == null)
            throw new EntryNotFound(appCode);

        // checking for duplicate entry
        ApplicationManagement obj = repo.findByAppCode(newApplication.getAppCode()).orElse(null);
        if(obj != null && !obj.getAppCode().equals(application.getAppCode()))
        throw new EntryAlreadyExists(obj.getAppCode(), obj.getAppName());

        // checking for invalid contacts
        List<String> contacts = Arrays.asList(newApplication.getContacts().split(";[ ]*"));
        if(!newApplication.getContacts().equals(""))
        for(String contact: contacts)
        if (!EmailValidation.emailValidator(contact)) throw new EmailNotValidException(contact);
        
        application.setAppCode(newApplication.getAppCode());
        application.setAppName(newApplication.getAppName());
        application.setContacts(newApplication.getContacts());

        repo.save(application);
        throw new AppUpdated(application.getAppCode(), application.getAppName());
    }
}