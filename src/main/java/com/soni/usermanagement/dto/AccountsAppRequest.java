package com.soni.usermanagement.dto;

public class AccountsAppRequest {

    private Long accountID;
    
    private String fileCode;
    private String applicationCode;
    private String fileTypeCode;

    private String lastUpdatedUserEmail;
    private String lastUpdatedDate;

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getFileTypeCode() {
        return fileTypeCode;
    }

    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
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