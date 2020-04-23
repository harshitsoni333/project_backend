package com.soni.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import com.soni.usermanagement.dto.AccountsAppResponse;
import com.soni.usermanagement.model.AccountsAppModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsAppRepo extends JpaRepository<AccountsAppModel, Long> {

    @Query(nativeQuery = true,
        value = "select aar.id, a.account_code, a.iban, a.bank_code, a.entity, fm.is_kmt54, aar.format, aar.last_updated_user, aar.last_updated_date, f.file_code, f.app_code, faft.file_type_code " +
        "from file_type_management fm inner join (file_app_relation f inner join(file_app_filetype faft inner join (accounts a inner join account_app_relation aar on a.account_code = aar.account_code) on aar.file_app_id = faft.id) on f.id = faft.file_app_id) on fm.file_type_code = faft.file_type_code"
    )
    public List<AccountsAppResponse> getInnerJoin();

    @Query(nativeQuery = true,
        value = " select * from account_app_relation where account_id = ?1 and file_app_id = ?2")
    public Optional<AccountsAppModel> findByAccountFileAppCombo(Long accountID, Long fileAppID);

    @Query(nativeQuery = true,
        value = "select exists(select * from account_app_relation where account_code = ?1)")
    public Long ifAccountExists(String accountCode);


    @Query(nativeQuery = true,
        value = "select count(distinct tab.file_code, tab.app_code) from (select aar.id, a.account_code, a.iban, a.bank_code, a.entity, fm.is_kmt54, aar.format, aar.last_updated_user, aar.last_updated_date, f.file_code, f.app_code, faft.file_type_code from file_type_management fm inner join (file_app_relation f inner join(file_app_filetype faft inner join (accounts a inner join account_app_relation aar on a.account_code = aar.account_code) on aar.file_app_id = faft.id) on f.id = faft.file_app_id) on fm.file_type_code = faft.file_type_code and a.account_code = ?1) as tab"
    )
    public Integer countApplications(String accountCode);
    
    @Query(nativeQuery = true,
        value = "select exists(select * from (select distinct tab.file_code, tab.app_code from (select aar.id, a.account_code, a.iban, a.bank_code, a.entity, fm.is_kmt54, aar.format, aar.last_updated_user, aar.last_updated_date, f.file_code, f.app_code, faft.file_type_code from file_type_management fm inner join (file_app_relation f inner join(file_app_filetype faft inner join (accounts a inner join account_app_relation aar on a.account_code = aar.account_code) on aar.file_app_id = faft.id) on f.id = faft.file_app_id) on fm.file_type_code = faft.file_type_code and a.account_code = ?1) as tab) as tab1 where tab1.file_code = ?2 and tab1.app_code = ?3)"
        )
    public Long ifFileAppExists(String accountCode, String fileCode, String appCode);

    @Query(nativeQuery = true,
        value = "select * from account_app_relation where account_code = ?1")
    public List<AccountsAppModel> findAllByAccountCode(String accountCode);

}