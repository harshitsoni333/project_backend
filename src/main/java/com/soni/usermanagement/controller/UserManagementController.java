package com.soni.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.methods.EmailMessage;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.methods.PasswordEncoder;
import com.soni.usermanagement.methods.PasswordGenerator;
import com.soni.usermanagement.model.ResponseMessage;
import com.soni.usermanagement.model.UserLogin;
import com.soni.usermanagement.model.UserManagement;
import com.soni.usermanagement.repository.UserLoginRepo;
import com.soni.usermanagement.repository.UserManagementRepo;
import com.soni.usermanagement.services.EmailService;

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
    @Autowired
    private EmailService emailService;

    @GetMapping("/users")
    public List<UserManagement> getAllUsers() {
        return repo.findAll();
    }

    @PostMapping(path = "/users", consumes = "application/json")
    public ResponseEntity<?> addUser(@RequestBody UserManagement newUser) {

        String email = newUser.getEmail();

        // checking if email valid
        if(!EmailValidation.emailValidator(email))
        throw new EmailNotValidException(email);

        // checking for duplicate entry
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user != null) throw new EntryAlreadyExists(user.getFirstName(), user.getEmail());

        // saving user
        repo.save(newUser);
        
        // adding new login details
        String password = PasswordGenerator.generatePassword();
        loginRepo.save(new UserLogin(
            newUser.getEmail(), PasswordEncoder.encodePassword(password), newUser.getProfile()));
        
        // sending a confirmation mail
        emailService.sendMail(
            newUser.getEmail(),
            EmailMessage.makeSubjectFor("create", newUser.getFirstName()),
            EmailMessage.makeMessageFor("create", newUser));

        // SENDING LOGIN DETAILS THROUGH MAIL
        emailService.sendMail(
            newUser.getEmail(), 
            EmailMessage.makeSubjectFor("login", newUser.getFirstName()), 
            EmailMessage.makePasswordMessageFor(
                "login", newUser.getFirstName(), newUser.getEmail(), password));
        
        return ResponseEntity.ok(new ResponseMessage(
            "New user added: " + newUser.getEmail()));
    }

    @PutMapping(path="/users/{email}", consumes = "application/json")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserManagement newUser, @PathVariable("email") String email) {
        
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

        //updating userName and profile in login_details
        UserLogin login = loginRepo.findByUserName(email).orElse(null);
        login.setUserName(newUser.getEmail());
        login.setProfile(newUser.getProfile());
        loginRepo.save(login);

        // sending a confirmation email
        emailService.sendMail(
                newUser.getEmail(),
                EmailMessage.makeSubjectFor("update", newUser.getFirstName()),
                EmailMessage.makeMessageFor("update", newUser));

        return ResponseEntity.ok(new ResponseMessage(
            "User details updated: " + user.getEmail()));
    }

    @GetMapping("/users/{email}")
    public UserManagement getUser(@PathVariable("email") String email) {
        
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user == null) throw new EntryNotFound(email);
        return user;
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        
        // checking user existence
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user == null) throw new EntryNotFound(email);
        
        repo.deleteById(user.getId());
        // deleting login details
        UserLogin login = loginRepo.findByUserName(email).orElse(null);
        loginRepo.deleteById(login.getId());

        emailService.sendMail(
                user.getEmail(),
                EmailMessage.makeSubjectFor("delete", user.getFirstName()),
                EmailMessage.makeMessageFor("delete", user));
        
        return ResponseEntity.ok(new ResponseMessage("User deleted: " + email));
    }
}