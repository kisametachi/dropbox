package com.dropbox.service;

import com.dropbox.constants.StorageType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageEngine {

  StorageType getStorageName();

  boolean saveFile(String path, MultipartFile file);

  boolean replaceFile(Path path, MultipartFile file);

  boolean deleteFile(String fileName);

  Resource readFile(String filePath);
}
