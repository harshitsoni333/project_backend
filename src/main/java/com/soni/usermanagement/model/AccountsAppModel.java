package com.soni.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account_app_relation")
public class AccountsAppModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "account_id")
    private Long accountID;

    @Column(name = "file_app_id")
    private Long fileAppID;

    @Column(name = "last_updated_user")
    private String lastUpdatedUserEmail;

    @Column(name = "last_updated_date")
    private String lastUpdatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public Long getFileAppID() {
        return fileAppID;
    }

    public void setFileAppID(Long fileAppID) {
        this.fileAppID = fileAppID;
    }

    public String getLastUpdatedUserEmail() {
        return lastUpdatedUserEmail;
    }

    public void setLastUpdatedUserEmail(String lastUpdatedUserEmail) {
        this.lastUpdatedUserEmail = lastUpdatedUserEmail;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

}