package com.soni.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.exception.error.EntryAlreadyExists;
import com.soni.usermanagement.exception.error.EntryNotFound;
import com.soni.usermanagement.methods.FileTypeValidator;
import com.soni.usermanagement.model.FileTypeManagement;
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

        if(FileTypeValidator.validateFileType(newFileType)) {

            // checking if entry already exists
            FileTypeManagement fileType = repo.findByFileTypeCode(newFileType.getFileTypeCode()).orElse(null);
            if(fileType != null) 
            throw new EntryAlreadyExists(fileType.getFileTypeCode(), fileType.getDescription());

            repo.save(newFileType);
            return new ResponseEntity<>("File type added: " + newFileType.getFileTypeCode(), HttpStatus.OK);
        }
        return new ResponseEntity<>("file type can't be added: invalid new entry", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/fileTypes/{fileTypeCode}")
    public ResponseEntity<String> deleteFileType(@PathVariable("fileTypeCode") String fileTypeCode) {
        
        // checking existense of fileType
        FileTypeManagement fileType = repo.findByFileTypeCode(fileTypeCode).orElse(null);
        if(fileType == null) throw new EntryNotFound(fileTypeCode);
        else repo.deleteById(fileType.getId());

        return new ResponseEntity<>("file type deleted: " + fileTypeCode, HttpStatus.OK);
    }

    @PutMapping("/fileTypes/{fileTypeCode")
    public ResponseEntity<String> updateFileType(@Valid @RequestBody FileTypeManagement newFileType, @PathVariable("fileTypeCode") String fileTypeCode) {
        
        // checking file type existence and deleting
        FileTypeManagement fileType = repo.findByFileTypeCode(fileTypeCode).orElse(null);
        if(fileType == null) throw new EntryNotFound(fileTypeCode);

        if(FileTypeValidator.validateFileType(newFileType)) {

            // checking for duplicate entry
            FileTypeManagement obj = repo.findByFileTypeCode(newFileType.getFileTypeCode()).orElse(null);
            if(obj != null && !obj.getFileTypeCode().equalsIgnoreCase(fileTypeCode))
            throw new EntryAlreadyExists(obj.getFileTypeCode(), obj.getDescription());

            // updating values
            fileType.setFileTypeCode(newFileType.getFileTypeCode());
            fileType.setDescription(newFileType.getDescription());
            fileType.setIsBankFile(newFileType.getIsBankFile());
            fileType.setIsKMT54(newFileType.getIsKMT54());

            return new ResponseEntity<>("file type updated: " + fileTypeCode, HttpStatus.OK);
        }
        return new ResponseEntity<>("entry can't be updated: new entry not valid", HttpStatus.NOT_ACCEPTABLE);
    }
}