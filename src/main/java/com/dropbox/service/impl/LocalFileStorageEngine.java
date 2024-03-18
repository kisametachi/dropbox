package com.dropbox.service.impl;

import com.dropbox.constants.StorageType;
import com.dropbox.service.FileStorageEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@Slf4j
public class LocalFileStorageEngine implements FileStorageEngine {

  @Override
  public StorageType getStorageName() {
    return StorageType.LOCAL;
  }

  @Override
  public boolean saveFile(String path, MultipartFile multipartFile) {
    try {
      Files.createDirectories(Paths.get(path));
      File file = new File(path);
      multipartFile.transferTo(file);
      return true;
    } catch (IOException e) {
      log.error("exception occurred while saving file {} to {}", multipartFile.getName(),
          StorageType.LOCAL, e);
    }
    return false;
  }

  @Override
  public boolean replaceFile(Path path, MultipartFile file) {
    try {
      Files.write(path, file.getBytes());
      Files.move(path, path.resolveSibling(file.getOriginalFilename()));
      return true;
    } catch (Exception e) {
      log.error("error updating file {} ", file.getName());
    }
    return false;
  }

  @Override
  public boolean deleteFile(String fileName) {
    try {
      Files.delete(Path.of(fileName));
      return true;
    } catch (Exception e) {
      log.error("exception occurred while deleting file {} from {}", fileName, StorageType.LOCAL);
    }
    return false;
  }

  @Override
  public Resource readFile(String filePath) {
    try {
      log.info("file reading started");
      return new ByteArrayResource(Files.readAllBytes(Path.of(filePath)));
    } catch (Exception ex) {
      log.error("error occurred while reading file ", ex);
    }
    return null;
  }
}
