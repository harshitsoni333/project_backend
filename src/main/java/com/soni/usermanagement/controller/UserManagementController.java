package com.soni.usermanagement.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import com.soni.usermanagement.exception.EmailAlreadyExists;
import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.NoUsersFoundException;
import com.soni.usermanagement.exception.UserNotFoundException;
import com.soni.usermanagement.model.UserManagement;
import com.soni.usermanagement.repository.UserManagementRepo;

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
public class UserManagementController {

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
    private UserManagementRepo repo;

    @GetMapping("/users")
    public List<UserManagement> getAllUsers() {

        List<UserManagement> users = repo.findAll();
        if(users.isEmpty()) {
            throw new NoUsersFoundException();
        }
        else return users;
    }

    @PostMapping(path = "/user", consumes = "application/json")
    public UserManagement addUser(@RequestBody UserManagement user) {

        String email = user.getEmail();

        if (emailValidator(email)) {
            // email is valid

            List<UserManagement> users = repo.findAll();
            for(UserManagement obj: users) {
                if(obj.getEmail().equals(email)) {
                    throw new EmailAlreadyExists(email);
                }
            }
            
            repo.save(user);
            return user;
		}
		else {
            //email is not valid
            throw new EmailNotValidException(email);
		}
    }

    @PutMapping(path="/user/{email}", consumes = "application/json")
    public UserManagement updateUser(@Valid @RequestBody UserManagement newUser, @PathVariable("email") String email) {
        
        UserManagement user = repo.findByEmail(email).orElse(null);

        if(user == null) {
            throw new UserNotFoundException(email);
        }

        if (emailValidator(newUser.getEmail())) {
            // email is valid
            user.setEmail(newUser.getEmail());
            user.setFirstname(newUser.getFirstname());
            user.setLastname(newUser.getLastname());
            user.setProfile(newUser.getProfile());
            repo.save(user);
            return user;
		}
		else {
            //email is not valid
            throw new EmailNotValidException(newUser.getEmail());
        }
    }

    @GetMapping("/user/{email}")
    public UserManagement getUser(@PathVariable("email") String email) {
        
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(email);
        }
        return user;
    }

    @DeleteMapping("/user/{email}")
    public UserManagement deleteUser(@PathVariable("email") String email) {
        
        UserManagement user = repo.findByEmail(email).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(email);
        }
        repo.deleteById(user.getId());
        return user;
    }
}