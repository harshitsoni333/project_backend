package com.soni.usermanagement.dto;

public class AccountsAppResponse {

    // account id and details
    private Long accountID;
    private String iban;
    private String bankCode;
    private String entity;

    // details from appli id
    private String fileCode;
    private String applicationCode;
    private String fileTypeCode;
    private String isKMT54;

    // from accounts_app
    private String format;
    private String lastUpdatedUserEmail;
    private String lastUpdatedDate;

    public AccountsAppResponse() {
    }

    public AccountsAppResponse(Long accountID, String iban, String bankCode, String entity, String fileCode,
            String applicationCode, String fileTypeCode, String isKMT54, String format, String lastUpdatedUserEmail,
            String lastUpdatedDate) {
        this.accountID = accountID;
        this.iban = iban;
        this.bankCode = bankCode;
        this.entity = entity;
        this.fileCode = fileCode;
        this.applicationCode = applicationCode;
        this.fileTypeCode = fileTypeCode;
        this.isKMT54 = isKMT54;
        this.format = format;
        this.lastUpdatedUserEmail = lastUpdatedUserEmail;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
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

    public String getIsKMT54() {
        return isKMT54;
    }

    public void setIsKMT54(String isKMT54) {
        this.isKMT54 = isKMT54;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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