package com.dropbox.service;

import com.dropbox.response.FileMetaDataResponse;
import com.dropbox.response.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManagementService {
  String uploadFile(MultipartFile file);

  FileResponseDTO getFile(String fileId);

  String updateFile(String fileId, MultipartFile file);

  String deleteFile(String fileId);

  List<FileMetaDataResponse> getAll();
}
