package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.AppManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppManagementRepo extends JpaRepository<AppManagement, Integer> {

    // user-defined methods
    Optional<AppManagement> findByAppCode(String appCode);

}