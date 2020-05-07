package com.soni.usermanagement.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.exception.UnauthorizedAccess;
import com.soni.usermanagement.methods.FileTypeValidator;
import com.soni.usermanagement.model.FileTypeManagement;
import com.soni.usermanagement.model.UserManagement;
import com.soni.usermanagement.repository.FileTypeManagementRepo;
import com.soni.usermanagement.repository.UserManagementRepo;

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
public class FileTypeManagementController {

    @Autowired
    private FileTypeManagementRepo repo;
    @Autowired
    private UserManagementRepo userManagementRepo;

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
    public ResponseEntity<?> addFileType(@Valid @RequestBody FileTypeManagement newFileType,
    Principal principal) {

        // checking authorization
        UserManagement userManagement = userManagementRepo.findByEmail(principal.getName()).orElse(null);
        if(!userManagement.getProfile().equalsIgnoreCase("admin") && 
        !userManagement.getProfile().equalsIgnoreCase("update_only")) {
            throw new UnauthorizedAccess(principal.getName());
        }

        if(FileTypeValidator.validateFileType(newFileType)) {

            // checking if entry already exists
            FileTypeManagement fileType = repo.findByFileTypeCode(newFileType.getFileTypeCode()).orElse(null);
            if(fileType != null) 
            throw new EntryAlreadyExists(fileType.getFileTypeCode(), fileType.getDescription());

            repo.save(newFileType);

            return ResponseEntity.ok(new ResponseMessage(
                "New file type added: " + newFileType.getFileTypeCode()));
        }
        else throw new InvalidEntry("File type can't be added. Check its contents again.");
    }

    @DeleteMapping("/fileTypes/{fileTypeCode}")
    public ResponseEntity<?> deleteFileType(@PathVariable("fileTypeCode") String fileTypeCode,
    Principal principal) {

        // checking authorization
        UserManagement userManagement = userManagementRepo.findByEmail(principal.getName()).orElse(null);
        if(!userManagement.getProfile().equalsIgnoreCase("admin") && 
        !userManagement.getProfile().equalsIgnoreCase("update_only")) {
            throw new UnauthorizedAccess(principal.getName());
        }
        
        // checking existense of fileType
        FileTypeManagement fileType = repo.findByFileTypeCode(fileTypeCode).orElse(null);
        if(fileType == null) throw new EntryNotFound(fileTypeCode);
        else repo.deleteById(fileType.getId());

        return ResponseEntity.ok(new ResponseMessage(
            "File type deleted: " + fileType.getFileTypeCode()));
    }

    @PutMapping("/fileTypes/{fileTypeCode}")
    public ResponseEntity<?> updateFileType(@Valid @RequestBody FileTypeManagement newFileType, 
    @PathVariable("fileTypeCode") String fileTypeCode, Principal principal) {

        // checking authorization
        UserManagement userManagement = userManagementRepo.findByEmail(principal.getName()).orElse(null);
        if(!userManagement.getProfile().equalsIgnoreCase("admin") && 
        !userManagement.getProfile().equalsIgnoreCase("update_only")) {
            throw new UnauthorizedAccess(principal.getName());
        }
        
        // checking file type existence and deleting
        FileTypeManagement fileType = repo.findByFileTypeCode(fileTypeCode).orElse(null);
        if(fileType == null) throw new EntryNotFound(fileTypeCode);

        if(FileTypeValidator.validateFileType(newFileType)) {

            // checking for duplicate entry
            FileTypeManagement obj = repo.findByFileTypeCode(newFileType.getFileTypeCode()).orElse(null);
            if(obj != null && !obj.getFileTypeCode().equals(fileType.getFileTypeCode()))
            throw new EntryAlreadyExists(obj.getFileTypeCode(), obj.getDescription());

            // updating values
            fileType.setFileTypeCode(newFileType.getFileTypeCode());
            fileType.setDescription(newFileType.getDescription());
            fileType.setIsBankFile(newFileType.getIsBankFile());
            fileType.setIsKMT54(newFileType.getIsKMT54());
            repo.save(fileType);

            return ResponseEntity.ok(new ResponseMessage(
                "File type updated: " + fileType.getFileTypeCode()));
        }
        else throw new InvalidEntry("File type can't be updated. Check updated contents again.");
    }
}