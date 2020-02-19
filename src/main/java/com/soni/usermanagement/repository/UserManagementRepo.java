package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.UserManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepo extends JpaRepository<UserManagement, Integer> {

    // user-defined methods
    Optional<UserManagement> findByEmail(String email);
}