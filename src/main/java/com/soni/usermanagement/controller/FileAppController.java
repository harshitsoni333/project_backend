package com.soni.usermanagement.controller;

import java.util.List;

import com.soni.usermanagement.dto.FileAppResponse;
import com.soni.usermanagement.repository.FileAppRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileAppController {

    @Autowired
    private FileAppRepo repo;

    @GetMapping("/getinnerjoin")
    public List<FileAppResponse> getInnerJoing() {
        return repo.getInnerJoinData();
    }
}