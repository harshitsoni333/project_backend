package com.soni.usermanagement.dto;

public class ContactManagementRequest {
    
    private Long id;
    
    private String appCode;

    private String fileCode;

    private String fileTypeCode;
    
    private String contacts;

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
    
}