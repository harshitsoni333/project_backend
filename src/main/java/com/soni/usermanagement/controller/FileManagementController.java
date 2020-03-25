package com.soni.usermanagement.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.model.FileManagement;
import com.soni.usermanagement.model.ResponseMessage;
import com.soni.usermanagement.repository.FileManagementRepo;

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
public class FileManagementController {

    @Autowired
    private FileManagementRepo repo;

    @GetMapping("/files")
    public List<FileManagement> getAllFiles() {
        return repo.findAll();
    }

    @PostMapping(path="/files", consumes = "application/json")
    public ResponseEntity<?> addFile(@RequestBody FileManagement newFile) {
        
        // CHECKING for INVALID E-MAILS
        List<String> emails = Arrays.asList(newFile.getContacts().split(";[ ]*"));
        for(String i: emails) 
        if (!EmailValidation.emailValidator(i)) throw new EmailNotValidException(i);

        // CHECKING for duplicate entry
        FileManagement file = repo.findByFileCode(newFile.getFileCode()).orElse(null);
        if(file != null) throw new EntryAlreadyExists(file.getFileCode(), file.getFileCode());

        repo.save(newFile);
        
        return ResponseEntity.ok(new ResponseMessage("New file added: " + newFile.getFileCode()));
    }

    @PutMapping(path = "/files/{fileCode}", consumes = "application/json")
    public ResponseEntity<?> updateFile(@Valid @RequestBody FileManagement newFile, @PathVariable("fileCode") String fileCode) {
        
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

        return ResponseEntity.ok(new ResponseMessage("File updated: " + file.getFileCode()));
    }

    @GetMapping("/files/{fileCode}")
    public FileManagement getFile(@PathVariable("fileCode") String fileCode) {

        FileManagement file = repo.findByFileCode(fileCode).orElse(null);
        if(file == null) throw new EntryNotFound(fileCode);
        else return file;
    }

    @DeleteMapping("/files/{fileCode}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileCode") String fileCode) {

        FileManagement file = repo.findByFileCode(fileCode).orElse(null);
        if(file == null) throw new EntryNotFound(fileCode);
        repo.deleteById(file.getId());

        return ResponseEntity.ok(new ResponseMessage("File deleted: " + file.getFileCode()));
    }
}