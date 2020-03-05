package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.FileTypeManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTypeManagementRepo extends JpaRepository<FileTypeManagement, Integer> {

    // user-defined methods
    Optional<FileTypeManagement> findByFileTypeCode(String fileTypeCode);
}