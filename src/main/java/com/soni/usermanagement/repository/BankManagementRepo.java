package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.BankManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankManagementRepo extends JpaRepository<BankManagement, Long> {

    // user-defined methods
    Optional<BankManagement> findByBankCode(String bankCode);
}