package com.soni.usermanagement.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.soni.usermanagement.repository.ContactManagementRepo;
import com.soni.usermanagement.services.ExcelExportService;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ExcelExportController
 */
@RestController
public class ExcelExportController {

    @Autowired
    private ContactManagementRepo repo;

    @GetMapping("/exportExcel")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=ContactManagement.xlsx");
        ByteArrayInputStream stream = ExcelExportService.contactListToExcelFile(repo.findAll());
        IOUtils.copy(stream, response.getOutputStream());
    }
    
}