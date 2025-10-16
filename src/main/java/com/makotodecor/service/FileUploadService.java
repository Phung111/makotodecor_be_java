package com.makotodecor.service;

import com.makotodecor.model.ImageUploadResponse;
import com.makotodecor.model.MultipleImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

  ImageUploadResponse uploadFile(MultipartFile file, String folder);

  MultipleImageUploadResponse uploadMultipleFiles(List<MultipartFile> files, String folder);

  void deleteFile(String publicId);

  void deleteImages(List<String> publicIds);
}
