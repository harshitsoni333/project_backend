package com.soni.usermanagement.dto;

import org.springframework.beans.factory.annotation.Value;

public interface AccountsAppResponse {

    @Value("#{target.id}")
    public Long getID();

    @Value("#{target.account_id}")
    public Long getAccountID();

    @Value("#{target.iban}")
    public String getIban();

    @Value("#{target.bank_code}")
    public String getBankCode();

    @Value("#{target.entity}")
    public String getEntity();

    @Value("#{target.is_kmt54}")
    public String getIsKMT54();

    @Value("#{target.last_updated_user}")
    public String getLastUpdatedUserEmail();

    @Value("#{target.last_updated_date}")
    public String getLastUpdatedDate();

    @Value("#{target.file_code}")
    public String getFileCode();

    @Value("#{target.app_code}")
    public String getApplicationCode();

    @Value("#{target.file_type_code}")
    public String getFileTypeCode();
    
}