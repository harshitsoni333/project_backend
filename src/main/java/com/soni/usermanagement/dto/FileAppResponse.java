package com.soni.usermanagement.dto;

public class FileAppResponse {

    private Long id;
    private String fileCode;
    private String appCode;
    private String fileTypeCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getFileTypeCode() {
        return fileTypeCode;
    }

    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
    }

    public FileAppResponse(Long id, String fileCode, String appCode, String fileTypeCode) {
        this.id = id;
        this.fileCode = fileCode;
        this.appCode = appCode;
        this.fileTypeCode = fileTypeCode;
    }

}