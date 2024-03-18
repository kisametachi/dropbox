package com.dropbox.factory;


import com.dropbox.constants.StorageType;
import com.dropbox.service.FileStorageEngine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileStorageFactory {

  private final List<FileStorageEngine> fileStorageEngines;

  private Map<StorageType, FileStorageEngine> storageTypeFileStorageEngineMap;

  @PostConstruct
  public void init() {
    storageTypeFileStorageEngineMap = new EnumMap<>(StorageType.class);
    for (FileStorageEngine fileStorageEngine : fileStorageEngines) {
      storageTypeFileStorageEngineMap.put(fileStorageEngine.getStorageName(), fileStorageEngine);
    }
  }

  public FileStorageEngine getFileStorageEngine(StorageType storageType) {
    return storageTypeFileStorageEngineMap.get(storageType);
  }
}
