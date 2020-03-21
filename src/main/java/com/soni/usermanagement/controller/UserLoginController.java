package com.soni.usermanagement.controller;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.EntryAlreadyExists;
import com.soni.usermanagement.exception.error.EntryNotFound;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.model.ResponseMessage;
import com.soni.usermanagement.model.UserLogin;
import com.soni.usermanagement.repository.UserLoginRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserLoginController {

    @Autowired
    private UserLoginRepo repo;

    // @GetMapping("/logins")
    // public List<UserLogin> getAllLogins() {
    //     return repo.findAll();
    // }

    @PostMapping(path = "/logins", consumes = "application/json")
    public ResponseEntity<?> addLogin(@RequestBody UserLogin newLogin) {

        String userName = newLogin.getUserName();

        // checking if userName valid
        if(!EmailValidation.emailValidator(userName))
        throw new EmailNotValidException(userName);

        // checking for duplicate entry
        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login != null) throw new EntryAlreadyExists(login.getUserName(), login.getProfile());

        repo.save(newLogin);
        return ResponseEntity.ok(new ResponseMessage(
            "New login details added for " + newLogin.getUserName()));
    }

    @PutMapping(path="/logins/{userName}", consumes = "application/json")
    public ResponseEntity<?> updateLogin(@Valid @RequestBody UserLogin newLogin, @PathVariable("userName") String userName) {
        
        // get the existing login
        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login == null) throw new EntryNotFound(userName);

        // checking for invalid userName
        if(!EmailValidation.emailValidator(newLogin.getUserName()))
        throw new EmailNotValidException(newLogin.getUserName());

        //checking for duplicate entry
        UserLogin obj = repo.findByUserName(newLogin.getUserName()).orElse(null);
        if(obj != null && !obj.getUserName().equals(login.getUserName()))
        throw new EntryAlreadyExists(obj.getUserName(), obj.getProfile());

        login.setPassword(newLogin.getPassword());
        repo.save(login);
        return ResponseEntity.ok(new ResponseMessage(
            "Password updated for " + login.getUserName()));
    }

    // @GetMapping("/logins/{userName}")
    // public UserLogin getUser(@PathVariable("userName") String userName) {
        
    //     UserLogin login = repo.findByUserName(userName).orElse(null);
    //     if(login == null) throw new EntryNotFound(userName);
    //     return login;
    // }

    @DeleteMapping("/logins/{userName}")
    public ResponseEntity<?> deleteLogin(@PathVariable("userName") String userName) {
        
        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login == null) throw new EntryNotFound(userName);
        repo.deleteById(login.getId());

        return ResponseEntity.ok(new ResponseMessage(
            "Login details deleted for " + login.getUserName()));
    }
}