package com.dropbox.service.impl;

import com.dropbox.constants.StorageType;
import com.dropbox.service.FileStorageEngine;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
public class GoogleFileStorageEngine implements FileStorageEngine {


  @Override
  public StorageType getStorageName() {
    return StorageType.GOOGLE_CLOUD;
  }

  @Override
  public boolean saveFile(String path, MultipartFile file) {
    return false;
  }

  @Override
  public boolean replaceFile(Path path, MultipartFile file) {
    return false;
  }


  @Override
  public boolean deleteFile(String fileName) {
    return false;
  }

  @Override
  public Resource readFile(String filePath) {
    return null;
  }

}
