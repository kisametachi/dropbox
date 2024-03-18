package com.dropbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Table(name = "file_meta_data")
@Entity
@Data
public class FileMetaData {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long fileId;

  @Column(unique = true)
  private String fileName;
  private String filePath;
  private String fileType;
  private Long fileSize;
  private Date createdAt;
}
