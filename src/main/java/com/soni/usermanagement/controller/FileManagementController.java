package com.soni.usermanagement.controller;

import java.util.List;
import java.util.Arrays;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.EntryAlreadyExists;
import com.soni.usermanagement.exception.error.EntryNotFound;
import com.soni.usermanagement.exception.success.FileDeleted;
import com.soni.usermanagement.exception.success.FileUpdated;
import com.soni.usermanagement.exception.success.NewFileAdded;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.model.FileManagement;
import com.soni.usermanagement.repository.FileManagementRepo;

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
public class FileManagementController {

    @Autowired
    private FileManagementRepo repo;

    @GetMapping("/file")
    public List<FileManagement> getAllFiles() {
        return repo.findAll();
    }

    @PostMapping(path="/file", consumes = "application/json")
    public void addFile(@RequestBody FileManagement newFile) {
        
        // CHECKING for INVALID E-MAILS
        List<String> emails = Arrays.asList(newFile.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if (!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        // CHECKING for duplicate entry
        FileManagement file = repo.findByFileCode(newFile.getFileCode()).orElse(null);
        if(file != null) throw new EntryAlreadyExists(file.getFileCode(), file.getFileCode());

        repo.save(newFile);
        throw new NewFileAdded(newFile.getFileCode(), newFile.getFileName());
    }

    @PutMapping(path = "/file/{fileCode}", consumes = "application/json")
    public void updateFile(@Valid @RequestBody FileManagement newFile, @PathVariable("fileCode") String fileCode) {
        
        FileManagement file = repo.findByFileCode(fileCode).orElse(null);
        if(file == null) throw new EntryNotFound(fileCode);

        // checking for duplicate entry
        FileManagement obj = repo.findByFileCode(newFile.getFileCode()).orElse(null);
        if(obj != null && !obj.getFileCode().equals(file.getFileCode()))
        throw new EntryAlreadyExists(obj.getFileCode(), obj.getFileName());

        // checking for invalid emails
        List<String> emails = Arrays.asList(newFile.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if(!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        file.setFileCode(newFile.getFileCode());
        file.setFileName(newFile.getFileName());
        file.setDescription(newFile.getDescription());
        file.setContacts(newFile.getContacts());

        repo.save(file);
        throw new FileUpdated(file.getFileCode(), file.getFileName());
    }

    @GetMapping("/file/{fileCode}")
    public FileManagement getFile(@PathVariable("fileCode") String fileCode) {

        FileManagement file = repo.findByFileCode(fileCode).orElse(null);
        if(file == null) throw new EntryNotFound(fileCode);
        else return file;
    }

    @DeleteMapping("/file/{fileCode}")
    public void deleteFile(@PathVariable("fileCode") String fileCode) {

        FileManagement file = repo.findByFileCode(fileCode).orElse(null);
        if(file == null) throw new EntryNotFound(fileCode);
        repo.deleteById(file.getId());
        throw new FileDeleted(file.getFileCode(), file.getFileName());
    }
}