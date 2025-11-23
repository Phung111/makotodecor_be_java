package com.makotodecor.util;

import com.makotodecor.exceptions.FileUploadException;
import com.makotodecor.exceptions.base.ErrorMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileValidationUtils {

  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
  private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
      "image/jpeg",
      "image/jpg",
      "image/png",
      "image/gif",
      "image/webp"
  );
  private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
      "jpg",
      "jpeg",
      "png",
      "gif",
      "webp"
  );

  private FileValidationUtils() {
  }

  public static void validateFile(MultipartFile file) {
    FileUploadException exception = new FileUploadException();

    validateFileNotEmpty(file, "file", exception);
    validateFileSize(file, "file", exception);
    validateFileType(file, "file", exception);

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  public static void validateFiles(List<MultipartFile> files) {
    if (files == null || files.isEmpty()) {
      throw new FileUploadException(ErrorMessage.FILE_NO_FILES_PROVIDED);
    }

    FileUploadException exception = new FileUploadException();

    for (int i = 0; i < files.size(); i++) {
      MultipartFile file = files.get(i);
      String target = "files[" + i + "]";

      validateFileNotEmpty(file, target, exception);
      validateFileSize(file, target, exception);
      validateFileType(file, target, exception);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  private static void validateFileNotEmpty(MultipartFile file, String target,
      FileUploadException exception) {
    if (file == null || file.isEmpty()) {
      exception.addError(target, ErrorMessage.FILE_EMPTY);
    }
  }

  private static void validateFileSize(MultipartFile file, String target,
      FileUploadException exception) {
    if (file == null) {
      return;
    }

    long fileSize = file.getSize();
    if (fileSize > MAX_FILE_SIZE) {
      exception.addError(target, ErrorMessage.FILE_TOO_LARGE);
    }
  }

  private static void validateFileType(MultipartFile file, String target,
      FileUploadException exception) {
    if (file == null) {
      return;
    }

    String contentType = file.getContentType();
    String fileName = file.getOriginalFilename();

    boolean isValidContentType = contentType != null
        && ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase());

    boolean isValidExtension = false;
    if (fileName != null && fileName.contains(".")) {
      String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      isValidExtension = ALLOWED_EXTENSIONS.contains(extension);
    }

    if (!isValidContentType && !isValidExtension) {
      exception.addError(target, ErrorMessage.FILE_INVALID_TYPE);
    }
  }
}


