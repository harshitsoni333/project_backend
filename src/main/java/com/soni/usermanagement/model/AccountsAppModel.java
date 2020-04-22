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

    public static Long count = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "file_app_id")
    private Long fileAppID;

    @Column(name = "format")
    private String format;

    @Column(name = "last_updated_user")
    private String lastUpdatedUserEmail;

    @Column(name = "last_updated_date")
    private String lastUpdatedDate;

    public AccountsAppModel() {
        // this.id = count++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public AccountsAppModel(String accountCode, Long fileAppID, String format, String lastUpdatedUserEmail,
            String lastUpdatedDate) {
        this.id = count++;
        this.accountCode = accountCode;
        this.fileAppID = fileAppID;
        this.format = format;
        this.lastUpdatedUserEmail = lastUpdatedUserEmail;
        this.lastUpdatedDate = lastUpdatedDate;
    }

}