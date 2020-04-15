package com.soni.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.exception.PasswordNotValid;
import com.soni.usermanagement.methods.EmailMessage;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.methods.PasswordEncoder;
import com.soni.usermanagement.methods.PasswordGenerator;
import com.soni.usermanagement.methods.PasswordValidator;
import com.soni.usermanagement.model.UserLogin;
import com.soni.usermanagement.model.UserManagement;
import com.soni.usermanagement.repository.UserLoginRepo;
import com.soni.usermanagement.repository.UserManagementRepo;
import com.soni.usermanagement.services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserLoginController {

    @Autowired
    private UserLoginRepo repo;
    @Autowired
    private UserManagementRepo userRepo;
    @Autowired
    private EmailService emailService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    

    @GetMapping("/logins")
    public List<UserLogin> getAllLogins() {
        return repo.findAll();
    }

    @PostMapping(path = "/logins", consumes = "application/json")
    public ResponseEntity<?> addLogin(@RequestBody UserLogin newLogin) {

        String userName = newLogin.getUserName();

        // checking for existence in user_management
        UserManagement user = userRepo.findByEmail(userName).orElse(null);
        if(user == null) throw new EntryNotFound(userName + " (User not present in UserManagement)");

        // checking if userName valid
        if(!EmailValidation.emailValidator(userName))
        throw new EmailNotValidException(userName);

        // checking for duplicate entry
        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login != null) throw new EntryAlreadyExists(login.getUserName(), login.getProfile());
        
        // validating password
        if(!PasswordValidator.validate(newLogin.getPassword()))
        throw new PasswordNotValid(newLogin.getPassword());

        // encoding password
        newLogin.setPassword(PasswordEncoder.encodePassword(newLogin.getPassword()));

        // save login details
        repo.save(newLogin);

        return ResponseEntity.ok(new ResponseMessage(
            "New login details added for " + newLogin.getUserName()));
    }

    @PutMapping(path="/logins/{userName}", consumes = "application/json")
    public ResponseEntity<?> updateLogin(@Valid @RequestBody UserLogin newLogin, @PathVariable("userName") String userName) {
        
        // get the existing login
        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login == null) throw new EntryNotFound(userName);

        // find user in usermanagement
        UserManagement user = userRepo.findByEmail(userName).orElse(null);

        // checking for invalid userName
        if(!EmailValidation.emailValidator(newLogin.getUserName()))
        throw new EmailNotValidException(newLogin.getUserName());

        //checking for duplicate entry
        UserLogin obj = repo.findByUserName(newLogin.getUserName()).orElse(null);
        if(obj != null && !obj.getUserName().equals(login.getUserName()))
        throw new EntryAlreadyExists(obj.getUserName(), obj.getProfile());

        // validating password
        if(!PasswordValidator.validate(newLogin.getPassword()))
        throw new PasswordNotValid(newLogin.getPassword());

        // checking if new password is same as old one
        if(encoder.matches(newLogin.getPassword(), login.getPassword()))
        throw new InvalidEntry("Password cannot be same as previous one");

        // encrypting and saving password
        login.setPassword(PasswordEncoder.encodePassword(newLogin.getPassword()));
        repo.save(login);

        // sending a mail for details
        emailService.sendMail(
            login.getUserName(), 
            EmailMessage.makeSubjectFor("changePassword", user.getFirstName()), 
            EmailMessage.makePasswordMessageFor(
                "changePassword", user.getFirstName(), login.getUserName(), newLogin.getPassword()));
        
        return ResponseEntity.ok(new ResponseMessage(
            "Password updated for " + login.getUserName()));
    }

    @GetMapping("/logins/{userName}")
    public UserLogin getUser(@PathVariable("userName") String userName) {
        
        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login == null) throw new EntryNotFound(userName);
        return login;
    }

    @DeleteMapping("/logins/{userName}")
    public ResponseEntity<?> deleteLogin(@PathVariable("userName") String userName) {
        
        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login == null) throw new EntryNotFound(userName);

        UserManagement user = userRepo.findByEmail(userName).orElse(null);
        if(user == null) repo.deleteById(login.getId());
        else throw new InvalidEntry(
            userName + "(login details can't be deleted because user is present in UserManagement)");

        return ResponseEntity.ok(new ResponseMessage(
            "Login details deleted for " + login.getUserName()));
    }

    @RequestMapping("/forgotPassword/{userName}")
    public ResponseEntity<?> resetPassword(@PathVariable("userName") String userName) {

        // if username is valid
        if(!EmailValidation.emailValidator(userName))
        throw new EmailNotValidException(userName);

        // if user exists
        UserManagement user = userRepo.findByEmail(userName).orElse(null);
        if(user == null) throw new EntryNotFound(userName);

        // find login
        UserLogin login = repo.findByUserName(userName).orElse(null);

        //creating and encoding temporary password
        String tempPassword = PasswordGenerator.generatePassword();
        String encodedPassword = PasswordEncoder.encodePassword(tempPassword);

        if(login == null) login = new UserLogin(1, userName, encodedPassword, user.getProfile());
        else login.setPassword(encodedPassword);

        // save temp password
        repo.save(login);

        // send a reset mail with new password
        emailService.sendMail(
            userName, 
            EmailMessage.makeSubjectFor("forgotPassword", user.getFirstName()),
            EmailMessage.makePasswordMessageFor(
                "forgotPassword", user.getFirstName(), userName, tempPassword));

        return ResponseEntity.ok(new ResponseMessage(
            "A password reset mail has been sent to " + login.getUserName()));
    }
}