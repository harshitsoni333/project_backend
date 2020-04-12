package com.soni.usermanagement.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import javassist.SerialVersionUID;

@Entity
@Table(name = "filemanagement")
public class FileManagement implements Serializable {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "filecode")
    private String fileCode;

    @Column(name = "filename")
    private String fileName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "contacts")
    private String contacts;

    @ManyToMany(targetEntity = FileAppModel.class, mappedBy = "id", fetch = FetchType.LAZY)
	private Set<FileAppModel> fileApps;

    public FileManagement() {
        this.contacts = "";
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

}   