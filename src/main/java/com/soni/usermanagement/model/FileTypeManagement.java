package com.soni.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "file_type_management")
public class FileTypeManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "file_type_code")
    private String fileTypeCode;

    @Column(name = "description")
    private String description;

    @Column(name = "is_bank_file")
    private String isBankFile;

    @Column(name = "is_kmt54")
    private String isKMT54;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileTypeCode() {
        return fileTypeCode;
    }

    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsBankFile() {
        return isBankFile;
    }

    public void setIsBankFile(String isBankFile) {
        this.isBankFile = isBankFile;
    }

    public String getIsKMT54() {
        return isKMT54;
    }

    public void setIsKMT54(String isKMT54) {
        this.isKMT54 = isKMT54;
    }

}