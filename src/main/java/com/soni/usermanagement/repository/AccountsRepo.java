package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.AccountsModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepo extends JpaRepository<AccountsModel, Long> {

    // user-defined methods
    Optional<AccountsModel> findByAccountCode(String accountCode);
    Optional<AccountsModel> findByIban(String iban);

}