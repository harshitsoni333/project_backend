package com.soni.usermanagement.dto;

import org.springframework.beans.factory.annotation.Value;

public interface LastUpdatedDetails {

    @Value("#{target.last_updated_user}")
    public String getLastUpdatedUserEmail();
    
    @Value("#{target.last_updated_date}")
    public String getLastUpdatedDate();

}