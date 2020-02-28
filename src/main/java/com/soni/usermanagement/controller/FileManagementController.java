package com.soni.usermanagement.controller;

import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.NoFilesFound;
import com.soni.usermanagement.exception.success.NewFileAdded;
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
    private FileManagementRepo repo;

    @GetMapping("/files")
    public List<FileManagement> getAllFiles() {
        List<FileManagement> files = repo.findAll();
        if(files.isEmpty()) throw new NoFilesFound();
        else return files;
    }

    @PostMapping(path="/file", consumes = "application/json")
    public FileManagement addFile(@RequestBody FileManagement file) {
        
        List<String> emails = Arrays.asList(file.getContacts().split(";[ ]*"));

        for(String i: emails) {
            if (!emailValidator(i)) {
                // email is not valid
                throw new EmailNotValidException(i);
            }
        }
         
        repo.save(file);
        throw new NewFileAdded(file.getFilecode(), file.getFilename());
    }

    @PutMapping(path = "/file/{filecode}", consumes = "application/json")
    public FileManagement updateFile(@Valid @RequestBody FileManagement newFile, @PathVariable("filecode") String filecode) {
        
        FileManagement file = repo.findByFilecode(filecode).orElse(null);

        List<String> emails = Arrays.asList(file.getContacts().split(";[ ]*"));

        for(String i: emails) {
            if (!emailValidator(i)) {
                // email is not valid
                throw new EmailNotValidException(i);
            }
        }

        file.setFilename(newFile.getFilename());
        file.setDescription(newFile.getDescription());
        file.setContacts(newFile.getContacts());

        repo.save(file);
        return file;
    }

    @GetMapping("/file/{filecode}")
    public FileManagement getFile(@PathVariable("filecode") String filecode) {

        FileManagement file = repo.findByFilecode(filecode).orElse(null);
        return file;
    }

    @DeleteMapping("file/{filecode}")
    public FileManagement deleteFile(@PathVariable("filecode") String filecode) {

        FileManagement file = repo.findByFilecode(filecode).orElse(null);
        repo.deleteById(file.getId());
        return file;
    }

}