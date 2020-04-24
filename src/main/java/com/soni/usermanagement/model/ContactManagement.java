package com.soni.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contact_management")
public class ContactManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "app_code")
    private String appCode;

    @Column(name = "file_code")
    private String fileCode;

    @Column(name = "file_type_code")
    private String fileTypeCode;
    
    @Column(name = "contacts")
    private String contacts;

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

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFileTypeCode() {
        return fileTypeCode;
    }

    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
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

    public ContactManagement() {
    }

    public ContactManagement(Long id, String appCode, String fileCode, String fileTypeCode, String contacts,
            String lastUpdatedUserEmail, String lastUpdatedDate) {
        this.id = id;
        this.appCode = appCode;
        this.fileCode = fileCode;
        this.fileTypeCode = fileTypeCode;
        this.contacts = contacts;
        this.lastUpdatedUserEmail = lastUpdatedUserEmail;
        this.lastUpdatedDate = lastUpdatedDate;
    }

}   