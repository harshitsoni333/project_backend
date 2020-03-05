package com.soni.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EntryAlreadyExists;
import com.soni.usermanagement.exception.error.EntryNotFound;
import com.soni.usermanagement.exception.error.InvalidEntry;
import com.soni.usermanagement.model.FileTypeManagement;
import com.soni.usermanagement.model.IsAlphaNumeric;
import com.soni.usermanagement.repository.FileTypeManagementRepo;

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
public class FileTypeManagementController {

    @Autowired
    private FileTypeManagementRepo repo;

    @GetMapping("/fileTypes")
    public List<FileTypeManagement> getAllFileTypes() {
        return repo.findAll();
    }

    @GetMapping("/fileTypes/{fileTypeCode}")
    public FileTypeManagement getFileType(@PathVariable("fileTypeCode") String fileTypeCode) {
        FileTypeManagement fileType = repo.findByFileTypeCode(fileTypeCode).orElse(null);
        if (fileType == null) throw new EntryNotFound(fileTypeCode);
        else return fileType;
    }

    @PostMapping("/fileTypes")
    public ResponseEntity<String> addFileType(@Valid @RequestBody FileTypeManagement newFileType) {

        // checking for alphanumeric value of fileTypeCode
        String fileTypeCode = newFileType.getFileTypeCode();
        if(!IsAlphaNumeric.isAlphaNumeric(fileTypeCode))
        throw new InvalidEntry(fileTypeCode);

        // checking for invalid values of isBankFile and isKMT54
        String isBankFile = newFileType.getIsBankFile();
        String isKMT54 = newFileType.getIsKMT54();
        if(!(isBankFile.equalsIgnoreCase("yes") || isBankFile.equalsIgnoreCase("no")))
        throw new InvalidEntry("isBankFile = " + isBankFile);
        if(!(isKMT54.equalsIgnoreCase("yes") || isKMT54.equalsIgnoreCase("no")))
        throw new InvalidEntry("isKMT54 = " + isKMT54);

        // checking if entry already exists
        FileTypeManagement fileType = repo.findByFileTypeCode(fileTypeCode).orElse(null);
        if(fileType != null) 
        throw new EntryAlreadyExists(fileType.getFileTypeCode(), fileType.getDescription());

        repo.save(newFileType);
        return new ResponseEntity<>("File type added: " + fileTypeCode, HttpStatus.OK);
    }

    // @DeleteMapping("/fileTypes/{fileTypeCode}")
    // public ResponseEntity<String> deleteFileType(@PathVariable("fileTypeCode") String fileTypeCode) {}

    // @PutMapping("/fileTypes/{fileTypeCode")
    // public ResponseEntity<String> updateFileType(@Valid @RequestBody FileTypeManagement newFileType, @PathVariable("fileTypeCode") String fileTypeCode) {}
}