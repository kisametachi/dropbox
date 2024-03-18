package com.dropbox.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaDataResponse {

  private Long fileId;

  private String fileName;

  private Long fileSize;

  private String fileType;

  private Date createdAt;
}
