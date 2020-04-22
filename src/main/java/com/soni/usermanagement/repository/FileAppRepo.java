package com.soni.usermanagement.repository;

import java.util.List;

import com.soni.usermanagement.dto.FileAppResponse;
import com.soni.usermanagement.model.FileAppFileTypeModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAppRepo extends JpaRepository<FileAppFileTypeModel, Long> {

    @Query(nativeQuery = false,
            value = "select new com.soni.usermanagement.dto.FileAppResponse(fa.id, f.fileCode, f.applicationCode, fa.fileTypeCode) " +
                    "from FileAppFileTypeModel fa INNER JOIN FileAppModel f " +
                    "ON fa.fileAppID = f.id"
    )
    public List<FileAppResponse> getInnerJoinData();


    @Query(nativeQuery = true,
        value = " select id from file_app_relation where file_code = ?1 and app_code = ?2")
    public Long getFileAppID(String fileCode, String appCode);

    @Query(nativeQuery = true,
        value = " select id from file_app_filetype where file_app_id = ?1 and file_type_code = ?2")
    public Long getFileAppFileTypeID(Long fileAppID, String fileTypeCode);

    @Query(nativeQuery = true,
        value = "select exists(select * from file_app_filetype where file_app_id = ?1 and file_type_code = ?2)")
    public Long ifComboPresent(Long fileAppID, String fileTypeCode);

}