package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.FileManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileManagementRepo extends JpaRepository<FileManagement, Integer> {
    
    //user-defined methods
    Optional<FileManagement> findByFileCode(String fileCode);
}