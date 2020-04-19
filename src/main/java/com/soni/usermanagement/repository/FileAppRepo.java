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
    List<FileAppResponse> getInnerJoinData();

}