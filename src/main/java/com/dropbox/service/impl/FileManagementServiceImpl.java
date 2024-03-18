package com.dropbox.service.impl;

import com.dropbox.constants.StorageType;
import com.dropbox.entity.FileMetaData;
import com.dropbox.factory.FileStorageFactory;
import com.dropbox.repository.FileMetaDataRepository;
import com.dropbox.response.FileMetaDataResponse;
import com.dropbox.response.FileResponseDTO;
import com.dropbox.service.FileManagementService;
import com.dropbox.service.FileStorageEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FileManagementServiceImpl implements FileManagementService {

  private static final String SUCCESS = "success";

  private final FileMetaDataRepository fileMetaDataRepository;
  private final FileStorageFactory fileStorageFactory;

  @Value("${localFileStoragePath}")
  public String localFilePath;

  @Override
  @Transactional
  public String uploadFile(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    if(fileMetaDataRepository.existsByFileName(fileName)) {
      return "failed: file with " + fileName + " already exists";
    }
    FileStorageEngine fileStorageEngine =
        fileStorageFactory.getFileStorageEngine(StorageType.LOCAL);
    if(fileStorageEngine.saveFile(localFilePath + "/" + fileName, file)) {
      FileMetaData fileMetaData = new FileMetaData();
      fileMetaData.setFileName(file.getOriginalFilename());
      fileMetaData.setFilePath(localFilePath + "/" + fileName);
      fileMetaData.setFileType(file.getContentType());
      fileMetaData.setFileSize(file.getSize());
      fileMetaData.setCreatedAt(new Date());
      fileMetaDataRepository.save(fileMetaData);
      return SUCCESS;
    }
    return "failed: error in upload file";
  }

  @Override
  @Transactional(readOnly = true)
  public FileResponseDTO getFile(String fileId) {
    FileMetaData fileMetaData = fileMetaDataRepository.findByFileId(Long.valueOf(fileId));
    if(fileMetaData == null) {
      return new FileResponseDTO();
    }
    String filePath = fileMetaData.getFilePath();
    FileStorageEngine fileStorageEngine =
        fileStorageFactory.getFileStorageEngine(StorageType.LOCAL);
    FileResponseDTO fileResponseDTO = new FileResponseDTO();
    fileResponseDTO.setResource(fileStorageEngine.readFile(filePath));
    fileResponseDTO.setFileName(fileMetaData.getFileName());
    return fileResponseDTO;
  }

  @Override
  @Transactional
  public String updateFile(String fileId, MultipartFile file) {
    FileMetaData fileMetaData = fileMetaDataRepository.findByFileId(Long.valueOf(fileId));
    String filePath = fileMetaData.getFilePath();
    FileStorageEngine fileStorageEngine =
        fileStorageFactory.getFileStorageEngine(StorageType.LOCAL);
    if(Objects.isNull(file.getOriginalFilename())) {
      return "failed: fileName is invalid";
    }
    if(!fileMetaData.getFileType().equals(file.getContentType())) {
      return "failed: fileType mismatch";
    }
    if(fileStorageEngine.replaceFile(Path.of(filePath), file)) {
      fileMetaData.setFilePath(Path.of(fileMetaData.getFilePath()).getParent().toString() + "/"
          + file.getOriginalFilename());
      fileMetaData.setFileName(file.getOriginalFilename());
      fileMetaData.setFileType(file.getContentType());
      fileMetaData.setFileSize(file.getSize());
      fileMetaDataRepository.save(fileMetaData);
      return SUCCESS;
    }
    return "failed: error in update file";
  }

  @Override
  @Transactional
  public String deleteFile(String fileId) {
    String filePath = fileMetaDataRepository.getFilePathByFileId(Long.valueOf(fileId));
    if(Objects.isNull(filePath)) {
      return "failed: file does not exist";
    }
    FileStorageEngine fileStorageEngine =
        fileStorageFactory.getFileStorageEngine(StorageType.LOCAL);
    if(fileStorageEngine.deleteFile(filePath)) {
      fileMetaDataRepository.deleteById(Long.valueOf(fileId));
      return SUCCESS;
    }
    return "failed: error in delete file";
  }

  @Override
  public List<FileMetaDataResponse> getAll() {
    List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findAll();
    return fileMetaDataList.stream().map(this::getFileMetaDataResponse).toList();
  }

  private FileMetaDataResponse getFileMetaDataResponse(FileMetaData fileMetaData) {
    return FileMetaDataResponse.builder().fileId(fileMetaData.getFileId())
        .fileName(fileMetaData.getFileName()).fileSize(fileMetaData.getFileSize())
        .fileType(fileMetaData.getFileType()).createdAt(fileMetaData.getCreatedAt()).build();
  }
}
