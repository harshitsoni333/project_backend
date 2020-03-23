package com.soni.usermanagement.repository;

import java.util.Optional;

import com.soni.usermanagement.model.UserLogin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepo extends JpaRepository<UserLogin, Integer> {

    // user-defined methods
    public Optional<UserLogin> findByUserName(String userName);
    public void deleteByUserName(String userName); 
}