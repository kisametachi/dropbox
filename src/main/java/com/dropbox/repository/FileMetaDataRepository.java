package com.dropbox.repository;

import com.dropbox.entity.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {

  FileMetaData findByFileId(Long fileId);

  FileMetaData findByFileName(String fileName);

  @Query("SELECT FMD.filePath FROM FileMetaData FMD WHERE FMD.fileId =:fileId")
  String getFilePathByFileId(@Param("fileId") Long fileId);

  boolean existsByFileName(String fileName);

}
