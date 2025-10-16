package com.makotodecor.controller;

import com.makotodecor.model.ImageUploadResponse;
import com.makotodecor.model.MultipleImageUploadResponse;
import com.makotodecor.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileUploadController implements FileUploadServiceApi {

  private final FileUploadService fileUploadService;

  @Override
  public ResponseEntity<ImageUploadResponse> _uploadImage(MultipartFile file, String folder) {
    log.info("Received upload request for file: {}", file.getOriginalFilename());

    ImageUploadResponse response = fileUploadService.uploadFile(file, folder);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<MultipleImageUploadResponse> _uploadMultipleImages(List<MultipartFile> files, String folder) {
    log.info("Received upload request for {} files", files != null ? files.size() : 0);

    MultipleImageUploadResponse response = fileUploadService.uploadMultipleFiles(files, folder);
    return ResponseEntity.ok(response);
  }
}
