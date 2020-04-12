package com.soni.usermanagement.repository;

import com.soni.usermanagement.model.AccountsAppModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsAppRepo extends JpaRepository<AccountsAppModel, Long> {

    // user-defined methods

}