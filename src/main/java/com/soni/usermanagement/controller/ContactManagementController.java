package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.EntryAlreadyExists;
import com.soni.usermanagement.exception.error.EntryNotFound;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.model.ContactManagement;
import com.soni.usermanagement.repository.ContactManagementRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class ContactManagementController {

    @Autowired
    private ContactManagementRepo repo;

    @GetMapping("/contacts")
    public List<ContactManagement> getAllContacts() {
        return repo.findAll();
    }

    @GetMapping("/contacts/{id}")
    public ContactManagement getContact(@PathVariable("id") Integer id) {
        ContactManagement contact = repo.findById(id).orElse(null);
        if (contact == null) throw new EntryNotFound(Integer.toString(id));
        else return contact;
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> addContact(@Valid @RequestBody ContactManagement newContact) {

        // CHECKING for INVALID E-MAILS
        List<String> emails = Arrays.asList(newContact.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if (!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        // cheking if entry already exists
        ContactManagement contact = repo.findById(newContact.getId()).orElse(null);
        if(contact != null) throw new EntryAlreadyExists(Integer.toString(contact.getId()), contact.getAppCode());
        else repo.save(newContact);
        
        return new ResponseEntity<>("Contact added: " + newContact.getId(), HttpStatus.OK);
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable("id") Integer id) {
        
        // checking existense of contact
        ContactManagement contact = repo.findById(id).orElse(null);
        if(contact == null) throw new EntryNotFound(Integer.toString(id));
        else repo.deleteById(id);

        return new ResponseEntity<>("Contact deleted: " + id, HttpStatus.OK);
    }

    @PutMapping("/contacts/{id}")
    public ResponseEntity<String> updateContact(@Valid @RequestBody ContactManagement newContact, @PathVariable("id") Integer id) {
        
        // checking Contact existence
        ContactManagement contact = repo.findById(id).orElse(null);
        if(contact == null) throw new EntryNotFound(Integer.toString(id));

        // CHECKING for INVALID E-MAILS
        List<String> emails = Arrays.asList(newContact.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if (!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        // updating values
        contact.setAppCode(newContact.getAppCode());
        contact.setFileCode(newContact.getFileCode());
        contact.setFileTypeCode(newContact.getFileTypeCode());
        contact.setContacts(newContact.getContacts());
        repo.save(contact);

        return new ResponseEntity<>("Contact updated: " + contact.getId(), HttpStatus.OK);
    }
}