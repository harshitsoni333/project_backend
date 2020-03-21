package com.soni.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "login_details")
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "user_email")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "profile")
    private String profile;

    public UserLogin(Integer id, String userName, String password, String profile) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.profile = profile;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public UserLogin() {
    }

    public UserLogin(String userName, String password, String profile) {
        this.userName = userName;
        this.password = password;
        this.profile = profile;
    }

}