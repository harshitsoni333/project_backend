package com.soni.usermanagement.repository;

import com.soni.usermanagement.model.FileAppModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAppModelRepo extends JpaRepository<FileAppModel, Long> {

    @Query(nativeQuery = true,
        value = "select exists(select * from file_app_relation where file_code = ?1 and app_code = ?2)")
    public Long ifFileAppPresent(String fileCode, String appCode);

}