package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.MethodNotAccepted;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.model.ContactManagement;
import com.soni.usermanagement.model.FileAppFileTypeModel;
import com.soni.usermanagement.model.FileAppModel;
import com.soni.usermanagement.repository.ContactManagementRepo;
import com.soni.usermanagement.repository.FileAppModelRepo;
import com.soni.usermanagement.repository.FileAppRepo;

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
public class ContactManagementController {

    @Autowired
    private ContactManagementRepo repo;
    @Autowired
    private FileAppModelRepo fileAppModelRepo;
    @Autowired
    private FileAppRepo fileAppRepo;

    @GetMapping("/contacts")
    public List<ContactManagement> getAllContacts() {
        return repo.findAll();
    }

    @GetMapping("/contacts/{id}")
    public ContactManagement getContact(@PathVariable("id") Long id) {
        ContactManagement contact = repo.findById(id).orElse(null);
        if (contact == null) throw new EntryNotFound(Long.toString(id));
        else return contact;
    }

    @PostMapping("/contacts")
    public ResponseEntity<?> addContact(@Valid @RequestBody ContactManagement newContact) {

        // CHECKING for INVALID E-MAILS
        List<String> emails = Arrays.asList(newContact.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if (!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        // cheking if entry already exists by id
        ContactManagement contact = repo.findById(newContact.getId()).orElse(null);
        if(contact != null) throw new EntryAlreadyExists(Long.toString(contact.getId()), contact.getAppCode());
        
        // check if file-app-filetype exists already
        // get all contacts
        List<ContactManagement> contacts = repo.findAll();
        // find file-app-filetype combo
        contact = contacts.stream()
            .filter(obj -> obj.getFileCode().equals(newContact.getFileCode())
                        && obj.getAppCode().equals(newContact.getAppCode())
                        && obj.getFileTypeCode().equals(newContact.getFileTypeCode()))
            .findAny()
            .orElse(null);

        // throw if combo exists already
        if(contact != null)
        throw new EntryAlreadyExists(
            "File-app-filetype combination already exists with id: ", Long.toString(contact.getId()));

        // save new contact
        repo.save(newContact);

        // save file-app combo
        if(fileAppModelRepo.ifFileAppPresent(
            newContact.getFileCode(), newContact.getAppCode()) == 0L)
        fileAppModelRepo.save(new FileAppModel(newContact.getFileCode(),newContact.getAppCode()));
        
        // save file-app-filetype combo
        Long fileAppID = fileAppRepo.getFileAppID(newContact.getFileCode(),newContact.getAppCode());
        if(fileAppRepo.ifComboPresent(fileAppID, newContact.getFileTypeCode()) == 0L)
        fileAppRepo.save(new FileAppFileTypeModel(fileAppID, newContact.getFileTypeCode()));

        return ResponseEntity.ok(new ResponseMessage(
            "New Contact added"));
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable("id") Long id) {
        
        // checking existense of contact
        ContactManagement contact = repo.findById(id).orElse(null);
        if(contact == null) throw new EntryNotFound(Long.toString(id));

        // delete file-app-filetype combo
        try {
            Long fileAppID = fileAppRepo.getFileAppID(contact.getFileCode(),contact.getAppCode());
            Long fileAppFileTypeID = fileAppRepo.getFileAppFileTypeID(fileAppID, contact.getFileTypeCode());
            fileAppRepo.deleteById(fileAppFileTypeID);
        } catch (Exception e) {
            throw new MethodNotAccepted("Cannot delete. This entry is in use in another table.");
        }

        // finally delete contact
        repo.deleteById(id);

        return ResponseEntity.ok(new ResponseMessage(
            "Contact deleted: " + contact.getId()));
    }

    @PutMapping("/contacts/{id}")
    public ResponseEntity<?> updateContact(@Valid @RequestBody ContactManagement newContact, @PathVariable("id") Long id) {
        
        // checking Contact existence
        ContactManagement contact = repo.findById(id).orElse(null);
        if(contact == null) throw new EntryNotFound(Long.toString(id));

        // CHECKING for INVALID E-MAILS
        List<String> emails = Arrays.asList(newContact.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if (!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        //checking for existing file-app-filetype combo
        List<ContactManagement> contacts = repo.findAll();
        // find file-app-filetype combo
        ContactManagement existingContact = contacts.stream()
            .filter(obj -> obj.getFileCode().equals(contact.getFileCode())
                        && obj.getAppCode().equals(contact.getAppCode())
                        && obj.getFileTypeCode().equals(newContact.getFileTypeCode()))
            .findAny()
            .orElse(null);

        // throw if combo exists already
        if(existingContact != null)
        throw new EntryAlreadyExists(
            "File-app-filetype combination already exists with id: ", 
            Long.toString(existingContact.getId()));

        // try deleteing old file-app-filetype combo
        try {
            Long fileAppID = fileAppRepo.getFileAppID(contact.getFileCode(),contact.getAppCode());
            Long fileAppFileTypeID = fileAppRepo.getFileAppFileTypeID(fileAppID, contact.getFileTypeCode());
            fileAppRepo.deleteById(fileAppFileTypeID);
        } catch (Exception e) {
            throw new MethodNotAccepted("Contact can't be updated, it is in use in another table.");
        }
        repo.deleteById(contact.getId());

        // updating values
        contact.setFileTypeCode(newContact.getFileTypeCode());
        contact.setContacts(newContact.getContacts());
        repo.save(contact);

        // add new file-app-filetype combo
        Long fileAppID = fileAppRepo.getFileAppID(contact.getFileCode(),contact.getAppCode());
        fileAppRepo.save(new FileAppFileTypeModel(fileAppID, contact.getFileTypeCode()));

        return ResponseEntity.ok(new ResponseMessage(
            "Contact updated: " + contact.getId()));
    }

}