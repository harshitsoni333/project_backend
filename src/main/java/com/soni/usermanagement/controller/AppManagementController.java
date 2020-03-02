package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.AppAlreadyExists;
import com.soni.usermanagement.exception.error.AppNotFound;
import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.success.AppDeleted;
import com.soni.usermanagement.exception.success.AppUpdated;
import com.soni.usermanagement.exception.success.NewAppAdded;
import com.soni.usermanagement.model.AppManagement;
import com.soni.usermanagement.model.EmailValidation;
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

    @Autowired
    private AppManagementRepo repo;

    @GetMapping("/apps")
    public List<AppManagement> getAllApps() {
        return repo.findAll();
    }

    @GetMapping("/apps/{appCode}")
    public AppManagement getApp(@PathVariable("appCode") String appCode) {
        AppManagement app = repo.findByAppCode(appCode).orElse(null);
        if(app == null) throw new AppNotFound(appCode);
        else return app;
    }

    @PostMapping("/apps")
    public void addApp(@RequestBody AppManagement newApp) {

        // checking for invalid e-mails
        List<String> contacts = Arrays.asList(newApp.getContacts().split(";[ ]*"));
        if(!newApp.getContacts().equals(""))
        for(String contact: contacts)
        if(!EmailValidation.emailValidator(contact)) throw new EmailNotValidException(contact);

        // checking if entry already exists
        AppManagement app = repo.findByAppCode(newApp.getAppCode()).orElse(null);
        if(app != null) throw new AppAlreadyExists(app.getAppCode(), app.getAppName());

        repo.save(newApp);
        throw new NewAppAdded(newApp.getAppCode(), newApp.getAppName());
    }

    @DeleteMapping("/apps/{appCode}")
    public void deleteApp(@PathVariable("appCode") String appCode) {
        AppManagement app = repo.findByAppCode(appCode).orElse(null);
        if(app == null) throw new AppNotFound(appCode);
        repo.deleteById(app.getId());
        throw new AppDeleted(app.getAppCode(), app.getAppName());
    }

    @PutMapping("/apps/{appCode}")
    public void updateApp(@Valid @RequestBody AppManagement newApp, @PathVariable("appCode") String appCode) {
        AppManagement app = repo.findByAppCode(appCode).orElse(null);

        if(app == null)
            throw new AppNotFound(appCode);

        // checking for duplicate entry
        AppManagement obj = repo.findByAppCode(newApp.getAppCode()).orElse(null);
        if(obj != null && !obj.getAppCode().equals(app.getAppCode()))
        throw new AppAlreadyExists(obj.getAppCode(), obj.getAppName());

        // checking for invalid emails
        List<String> emails = Arrays.asList(newApp.getContacts().split(";[ ]*"));
        if(!newApp.getContacts().equals(""))
        for(String email: emails)
        if (!EmailValidation.emailValidator(email)) throw new EmailNotValidException(email);
        
        app.setAppCode(newApp.getAppCode());
        app.setAppName(newApp.getAppName());
        app.setContacts(newApp.getContacts());

        repo.save(app);
        throw new AppUpdated(app.getAppCode(), app.getAppName());
    }
}