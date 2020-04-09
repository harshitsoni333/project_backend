package com.soni.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bank_id")
    private Long bankID;

    @Column(name = "entity")
    private String entity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Long getBankID() {
        return bankID;
    }

    public void setBankID(Long bankID) {
        this.bankID = bankID;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public AccountsModel(String accountCode, String iban, Long bankID, String entity) {
        this.accountCode = accountCode;
        this.iban = iban;
        this.bankID = bankID;
        this.entity = entity;
    }

    public AccountsModel() {
    }

    @Override
    public String toString() {
        return "[id = " + id + 
                ", accountCode = " + accountCode + 
                ", iban = "+ iban + 
                ", bankID = " + bankID + 
                ", entity = " + entity + "]";
    }
}