package com.makotodecor.controller;

import com.makotodecor.model.CreateImgRequest;
import com.makotodecor.model.ImgDetailResponse;
import com.makotodecor.model.ImgsPagedResponse;
import com.makotodecor.model.UpdateImgRequest;
import com.makotodecor.model.dto.ImgPagedCriteria;
import com.makotodecor.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ImgController implements ImgServiceApi {

  private final ImgService imgService;

  @Override
  public ResponseEntity<ImgsPagedResponse> _getImgsPaged(Integer page, Integer size, Long imgTypeId,
      String orderBy, Long productId, Boolean isDefault) {
    var criteria = ImgPagedCriteria.builder()
        .page(page)
        .size(size)
        .imgTypeId(imgTypeId)
        .orderBy(orderBy)
        .productId(productId)
        .isDefault(isDefault)
        .build();
    return ResponseEntity.ok(imgService.getImgsPaged(criteria));
  }

  @Override
  public ResponseEntity<ImgDetailResponse> _getImg(Long imgId) {
    return ResponseEntity.ok(imgService.getImg(imgId));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<ImgDetailResponse> _createImg(CreateImgRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(imgService.createImg(request));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<ImgDetailResponse> _updateImg(Long imgId, UpdateImgRequest request) {
    return ResponseEntity.ok(imgService.updateImg(imgId, request));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<String> _deleteImg(Long imgId) {
    imgService.deleteImg(imgId);
    return ResponseEntity.ok("Image deleted successfully");
  }
}
