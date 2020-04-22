package com.soni.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "file_app_filetype")
public class FileAppFileTypeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_app_id")
    private Long fileAppID;

    @Column(name = "file_type_code")
    private String fileTypeCode;

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

    public String getFileTypeCode() {
        return fileTypeCode;
    }

    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
    }

    public FileAppFileTypeModel(Long fileAppID, String fileTypeCode) {
        this.fileAppID = fileAppID;
        this.fileTypeCode = fileTypeCode;
    }

    public FileAppFileTypeModel() {
    }

}