package com.soni.usermanagement.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.methods.EmailMessage;
import com.soni.usermanagement.methods.EmailValidation;
import com.soni.usermanagement.repository.ContactManagementRepo;
import com.soni.usermanagement.services.EmailService;
import com.soni.usermanagement.services.ExcelExportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ExcelExportController
 */
@RestController
public class ExcelExportController {

    @Autowired
    private ContactManagementRepo repo;
    @Autowired
    private EmailService emailService;

    // @GetMapping("/exportExcel")
    // public void downloadCsv(HttpServletResponse response) throws IOException {
        
    //     response.setContentType("application/octet-stream");
    //     response.setHeader("Content-Disposition", "attachment; filename=ContactManagement.xlsx");
    //     ByteArrayInputStream stream = ExcelExportService.contactListToExcelFile(repo.findAll());
    //     IOUtils.copy(stream, response.getOutputStream());
    //}

    @PostMapping("/emailExcel")
    public ResponseEntity<?> emailCSV(@RequestBody String sendList, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=ContactManagement.xlsx");
        String filePath = ExcelExportService.contactListToExcelFile(repo.findAll());

        // validate all emails
        List<String> emails = Arrays.asList(sendList.split(",[ ]*"));
        for(String email: emails) 
        if (!EmailValidation.emailValidator(email)) throw new EmailNotValidException(email);

        // send emails to all recipients
        for(String email: emails) {
            emailService.sendMailWithAttachment(
                email, 
                EmailMessage.makeSubjectFor("exportExcel", email), 
                "Sending you the excel file as requested. \n\n" +
                "Hope you have a great day ahead. \nBest regards. \nTeam SUP",
                filePath);
        }

        return ResponseEntity.ok(new ResponseMessage("An email has been sent with the details."));
    }
    
}