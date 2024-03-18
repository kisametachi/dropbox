package com.dropbox.controller;

import com.dropbox.response.FileMetaDataResponse;
import com.dropbox.response.FileResponseDTO;
import com.dropbox.service.FileManagementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/file", produces = MediaType.TEXT_PLAIN_VALUE)
public class FileController {

  private static final String MULTIPART_FORM_DATA = "multipart/form-data";
  private static final String UPLOAD = "/upload";
  private static final String FILE_ID = "/{fileId}";
  private static final String GET_ALL = "/getAll";
  private final FileManagementService fileManagementService;

  @PostMapping(path = UPLOAD, consumes = MULTIPART_FORM_DATA)
  public String fileUpload(@RequestBody MultipartFile file) {
    return fileManagementService.uploadFile(file);
  }

  @GetMapping(path = FILE_ID, produces = MULTIPART_FORM_DATA)
  public ResponseEntity<Resource> getFile(@PathVariable("fileId") String fileId) {
    try {
      FileResponseDTO fileResponseDTO = fileManagementService.getFile(fileId);
      if(fileResponseDTO.getResource() == null) {
        return ResponseEntity.badRequest().build();
      }
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + fileResponseDTO.getFileName() + "\"");

      return ResponseEntity.ok().headers(headers).body(fileResponseDTO.getResource());
    } catch (Exception e) {
      log.error("error getting file {}, {}", fileId, e.getMessage(), e);
    }
    return ResponseEntity.badRequest().build();
  }

  @PutMapping(path = FILE_ID, consumes = MULTIPART_FORM_DATA)
  public String updateFile(@PathVariable("fileId") String fileId,
      @RequestBody MultipartFile multipartFile) {
    return fileManagementService.updateFile(fileId, multipartFile);
  }

  @DeleteMapping(path = FILE_ID)
  public String deleteFile(@PathVariable("fileId") String fileId) {
    return fileManagementService.deleteFile(fileId);
  }

  @GetMapping(path = GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FileMetaDataResponse>> getAllFiles() {
    return ResponseEntity.ok(fileManagementService.getAll());
  }
}
