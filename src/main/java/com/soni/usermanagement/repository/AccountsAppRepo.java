package com.soni.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import com.soni.usermanagement.dto.AccountsAppResponse;
import com.soni.usermanagement.dto.LastUpdatedDetails;
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
        value = "select last_updated_user, last_updated_date from account_app_relation " +
                " order by id desc limit 1")
    public LastUpdatedDetails findLastUpdatedDetails();

    @Query(nativeQuery = true,
        value = "select count(*) from account_app_relation where account_id = ?1")
    public Long findApplicationCount(Long accountID);

}