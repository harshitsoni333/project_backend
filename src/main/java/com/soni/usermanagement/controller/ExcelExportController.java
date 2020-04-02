package com.soni.usermanagement.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.soni.usermanagement.methods.EmailMessage;
import com.soni.usermanagement.repository.ContactManagementRepo;
import com.soni.usermanagement.services.EmailService;
import com.soni.usermanagement.services.ExcelExportService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void emailCSV(@RequestBody String email, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=ContactManagement.xlsx");
        String filePath = ExcelExportService.contactListToExcelFile(repo.findAll());



        // Scanner scanner = new Scanner(stream);
        // scanner.useDelimiter("\\Z");//To read all scanner content in one String
        // String data = "";
        // if (scanner.hasNext())
        //     data = scanner.next();
        
        // send an email
        emailService.sendMailWithAttachment(
            email, 
            EmailMessage.makeSubjectFor("exportExcel", email), 
            "Sending you the excel file as requested. \n\n" +
            "Hope you have a great day ahead. \nBest regards. \nTeam SUP",
            filePath);

    }
    
}