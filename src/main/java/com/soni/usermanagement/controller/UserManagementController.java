package com.soni.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.EntryAlreadyExists;
import com.soni.usermanagement.exception.error.EntryNotFound;
import com.soni.usermanagement.exception.success.NewUserAdded;
import com.soni.usermanagement.exception.success.UserUpdated;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.methods.PasswordEncoder;
import com.soni.usermanagement.model.ResponseMessage;
import com.soni.usermanagement.model.UserLogin;
import com.soni.usermanagement.model.UserManagement;
import com.soni.usermanagement.repository.UserLoginRepo;
import com.soni.usermanagement.repository.UserManagementRepo;

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
public class UserManagementController {

    @Autowired
    private UserManagementRepo repo;
    @Autowired
    private UserLoginRepo loginRepo;

    @GetMapping("/user")
    public List<UserManagement> getAllUsers() {
        return repo.findAll();
    }

    @PostMapping(path = "/user", consumes = "application/json")
    public void addUser(@RequestBody UserManagement newUser) {

        String email = newUser.getEmail();

        // checking if email valid
        if(!EmailValidation.emailValidator(email))
        throw new EmailNotValidException(email);

        // checking for duplicate entry
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user != null) throw new EntryAlreadyExists(user.getFirstName(), user.getEmail());

        try {
            repo.save(newUser);
            // adding new login details
            loginRepo.save(new UserLogin(newUser.getEmail(), PasswordEncoder.encodePassword("root"), newUser.getProfile()));
        
        } catch(Exception e) {

            // rolling back changes
            user = repo.findByEmail(newUser.getEmail()).orElse(null);
            repo.deleteById(user.getId());
            throw e;
        }
        
        throw new NewUserAdded(email);
    }

    @PutMapping(path="/user/{email}", consumes = "application/json")
    public void updateUser(@Valid @RequestBody UserManagement newUser, @PathVariable("email") String email) {
        
        // get the existing user
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user == null) throw new EntryNotFound(email);

        // checking for invalid email
        if(!EmailValidation.emailValidator(newUser.getEmail()))
        throw new EmailNotValidException(newUser.getEmail());

        //checking for duplicate entry
        UserManagement obj = repo.findByEmail(newUser.getEmail()).orElse(null);
        if(obj != null && !obj.getEmail().equals(user.getEmail()))
        throw new EntryAlreadyExists(obj.getFirstName(), obj.getEmail());

        user.setEmail(newUser.getEmail());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setProfile(newUser.getProfile());
        repo.save(user);
        throw new UserUpdated(newUser.getEmail());
    }

    @GetMapping("/user/{email}")
    public UserManagement getUser(@PathVariable("email") String email) {
        
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user == null) throw new EntryNotFound(email);
        return user;
    }

    @DeleteMapping("/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user == null) throw new EntryNotFound(email);
        
        repo.deleteById(user.getId());
        // deleteing login details
        loginRepo.deleteByUserName(email);

        return ResponseEntity.ok(new ResponseMessage("User deleted: " + email));
    }
}