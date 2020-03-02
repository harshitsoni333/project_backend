package com.soni.usermanagement.controller;

import java.util.List;
import java.util.Arrays;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.FileAlreadyExists;
import com.soni.usermanagement.exception.error.FileNotFound;
import com.soni.usermanagement.exception.success.FileDeleted;
import com.soni.usermanagement.exception.success.FileUpdated;
import com.soni.usermanagement.exception.success.NewFileAdded;
import com.soni.usermanagement.model.EmailValidation;
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

    @GetMapping("/files")
    public List<FileManagement> getAllFiles() {
        return repo.findAll();
    }

    @PostMapping(path="/files", consumes = "application/json")
    public void addFile(@RequestBody FileManagement newFile) {
        
        // CHECKING for INVALID E-MAILS
        List<String> emails = Arrays.asList(newFile.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if (!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        // CHECKING for duplicate entry
        FileManagement file = repo.findByFilecode(newFile.getFilecode()).orElse(null);
        if(file != null) throw new FileAlreadyExists(file.getFilecode(), file.getFilename());

        repo.save(newFile);
        throw new NewFileAdded(newFile.getFilecode(), newFile.getFilename());
    }

    @PutMapping(path = "/files/{filecode}", consumes = "application/json")
    public void updateFile(@Valid @RequestBody FileManagement newFile, @PathVariable("filecode") String filecode) {
        
        FileManagement file = repo.findByFilecode(filecode).orElse(null);
        if(file == null) throw new FileNotFound(filecode);

        // checking for duplicate entry
        FileManagement obj = repo.findByFilecode(newFile.getFilecode()).orElse(null);
        if(obj != null && !obj.getFilecode().equals(file.getFilecode()))
        throw new FileAlreadyExists(obj.getFilecode(), obj.getFilename());

        // checking for invalid emails
        List<String> emails = Arrays.asList(file.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if(!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        file.setFilecode(newFile.getFilecode());
        file.setFilename(newFile.getFilename());
        file.setDescription(newFile.getDescription());
        file.setContacts(newFile.getContacts());

        repo.save(file);
        throw new FileUpdated(file.getFilecode(), file.getFilename());
    }

    @GetMapping("/files/{filecode}")
    public FileManagement getFile(@PathVariable("filecode") String filecode) {

        FileManagement file = repo.findByFilecode(filecode).orElse(null);
        if(file == null) throw new FileNotFound(filecode);
        else return file;
    }

    @DeleteMapping("/files/{filecode}")
    public void deleteFile(@PathVariable("filecode") String filecode) {

        FileManagement file = repo.findByFilecode(filecode).orElse(null);
        if(file == null) throw new FileNotFound(filecode);
        repo.deleteById(file.getId());
        throw new FileDeleted(file.getFilecode(), file.getFilename());
    }
}