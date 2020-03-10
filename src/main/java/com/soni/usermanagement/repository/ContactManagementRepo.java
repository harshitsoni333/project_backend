package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.ContactManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactManagementRepo extends JpaRepository<ContactManagement, Integer> {

    // user-defined methods
    Optional<ContactManagement> findByAppCode(String appCode);
    Optional<ContactManagement> findByFileCode(String fileCode);
    Optional<ContactManagement> findByFileTypeCode(String fileTypeCode);

}