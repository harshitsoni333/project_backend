package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.AppAlreadyExists;
import com.soni.usermanagement.exception.error.AppNotFound;
import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.NoAppsFound;
import com.soni.usermanagement.exception.success.AppDeleted;
import com.soni.usermanagement.exception.success.AppUpdated;
import com.soni.usermanagement.exception.success.NewAppAdded;
import com.soni.usermanagement.model.AppManagement;
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

    @PostMapping("/apps")
    public void addApp(@RequestBody AppManagement newApp) {

        // checking for invalid e-mails
        List<String> contacts = Arrays.asList(newApp.getContacts().split(";[ ]*"));
        if(!newApp.getContacts().equals(""))
        for(String contact: contacts) {
            if(!emailValidator(contact)) {
                // e-mail is not valid
                throw new EmailNotValidException(contact);
            }
        }

        // checking if entry already exists
        String appCode = newApp.getAppCode();
        AppManagement app = repo.findByAppCode(appCode).orElse(null);
        if(app != null) throw new AppAlreadyExists(app.getAppCode(), app.getAppName());

        // List<AppManagement> apps = repo.findAll();
        // for(AppManagement app: apps) {
        //     if(app.getAppCode().equals(appCode)) {
        //         throw new AppAlreadyExists(app.getAppCode(), app.getAppName());
        //     }
        // }

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
        
        // List<AppManagement> apps = repo.findAll();
        // for(AppManagement obj: apps) {
        //     if(obj.getAppCode().equals(app.getAppCode())) continue;
        //     else if(obj.getAppCode().equals(newApp.getAppCode())) {
        //             throw new AppAlreadyExists(obj.getAppCode(), obj.getAppName());
        //     }
        // }

        // checking for invalid emails
        List<String> emails = Arrays.asList(newApp.getContacts().split(";[ ]*"));
        if(!newApp.getContacts().equals(""))
        for(String email: emails) {
            // if email not valid
            if (!emailValidator(email)) {
                throw new EmailNotValidException(email);
            }
        }

        app.setAppCode(newApp.getAppCode());
        app.setAppName(newApp.getAppName());
        app.setContacts(newApp.getContacts());

        repo.save(app);
        throw new AppUpdated(app.getAppCode(), app.getAppName());
    }
}