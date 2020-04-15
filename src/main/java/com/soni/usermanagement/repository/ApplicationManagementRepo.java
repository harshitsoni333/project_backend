package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.ApplicationManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationManagementRepo extends JpaRepository<ApplicationManagement, Long> {

    // user-defined methods
    Optional<ApplicationManagement> findByApplicationCode(String applicationCode);

}