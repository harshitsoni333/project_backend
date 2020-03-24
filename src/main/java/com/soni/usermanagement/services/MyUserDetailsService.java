package com.soni.usermanagement.services;

import java.util.ArrayList;

import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.methods.PasswordEncoder;
import com.soni.usermanagement.model.UserLogin;
import com.soni.usermanagement.repository.UserLoginRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserLoginRepo repo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        if(userName.equalsIgnoreCase("foo")) {
            return new User("foo", PasswordEncoder.encodePassword("foo"), new ArrayList<>());
        }

        UserLogin login = repo.findByUserName(userName).orElse(null);
        if(login == null) throw new EntryNotFound(userName);
        else return new User(login.getUserName(), login.getPassword(), new ArrayList<>());
    }
    
}