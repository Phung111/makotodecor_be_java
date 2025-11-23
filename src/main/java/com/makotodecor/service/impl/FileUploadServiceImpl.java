package com.makotodecor.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.makotodecor.exceptions.FileUploadException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.model.ImageUploadResponse;
import com.makotodecor.model.MultipleImageUploadResponse;
import com.makotodecor.service.FileUploadService;
import com.makotodecor.util.FileValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

  private final Cloudinary cloudinary;

  @Value("${cloudinary.upload.base-folder:makotodecor}")
  private String baseFolder;

  @Override
  public ImageUploadResponse uploadFile(MultipartFile file, String folder) {
    log.info("Uploading file to Cloudinary: {} to folder: {}", file.getOriginalFilename(), folder);

    FileValidationUtils.validateFile(file);

    try {
      String fullPath = buildFolderPath(folder);

      Map<String, Object> uploadParams = ObjectUtils.asMap(
          "resource_type", "image",
          "folder", fullPath);

      Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

      return buildImageUploadResponse(uploadResult);

    } catch (IOException e) {
      log.error("Failed to upload file to Cloudinary: {}", file.getOriginalFilename(), e);
      throw new FileUploadException(ErrorMessage.FILE_UPLOAD_FAILED);
    }
  }

  @Override
  public MultipleImageUploadResponse uploadMultipleFiles(List<MultipartFile> files, String folder) {
    log.info("Uploading {} files to Cloudinary folder: {}", files.size(), folder);

    FileValidationUtils.validateFiles(files);

    List<ImageUploadResponse> successfulUploads = new ArrayList<>();
    int failedCount = 0;

    for (MultipartFile file : files) {
      try {
        ImageUploadResponse response = uploadFile(file, folder);
        successfulUploads.add(response);
      } catch (FileUploadException e) {
        log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
        failedCount++;
      }
    }

    return MultipleImageUploadResponse.builder()
        .images(successfulUploads)
        .uploadedCount(successfulUploads.size())
        .failedCount(failedCount)
        .build();
  }

  @Override
  public void deleteFile(String publicId) {
    log.info("Deleting file from Cloudinary: {}", publicId);

    try {
      cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    } catch (IOException e) {
      log.error("Failed to delete file from Cloudinary: {}", publicId, e);
      throw new FileUploadException(ErrorMessage.FILE_UPLOAD_FAILED);
    }
  }

  @Override
  public void deleteImages(List<String> publicIds) {
    if (publicIds == null || publicIds.isEmpty()) {
      log.warn("No images to delete from Cloudinary");
      return;
    }

    log.info("Deleting {} images from Cloudinary", publicIds.size());

    int successCount = 0;
    int failCount = 0;

    for (String publicId : publicIds) {
      try {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        successCount++;
        log.debug("Successfully deleted image: {}", publicId);
      } catch (IOException e) {
        failCount++;
        log.error("Failed to delete image from Cloudinary: {}", publicId, e);
      }
    }

    log.info("Cloudinary deletion completed: {} successful, {} failed", successCount, failCount);

    if (failCount > 0 && successCount == 0) {
      throw new FileUploadException(ErrorMessage.FILE_UPLOAD_FAILED);
    }
  }

  private ImageUploadResponse buildImageUploadResponse(Map<?, ?> uploadResult) {
    return ImageUploadResponse.builder()
        .url((String) uploadResult.get("secure_url"))
        .publicId((String) uploadResult.get("public_id"))
        .width((Integer) uploadResult.get("width"))
        .height((Integer) uploadResult.get("height"))
        .format((String) uploadResult.get("format"))
        .resourceType((String) uploadResult.get("resource_type"))
        .createdAt((String) uploadResult.get("created_at"))
        .build();
  }

  private String buildFolderPath(String folder) {

    if (folder == null || folder.trim().isEmpty()) {
      return baseFolder;
    }

    folder = folder.trim();

    if (folder.startsWith(baseFolder + "/") || folder.equals(baseFolder)) {
      return folder;
    }

    return baseFolder + "/" + folder;
  }
}
