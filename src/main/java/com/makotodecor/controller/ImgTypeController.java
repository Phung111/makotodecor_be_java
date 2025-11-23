package com.makotodecor.controller;

import com.makotodecor.model.CreateImgTypeRequest;
import com.makotodecor.model.ImgTypeResponse;
import com.makotodecor.model.ImgTypesPagedResponse;
import com.makotodecor.model.UpdateImgTypeRequest;
import com.makotodecor.model.UpdateImgTypesStatusRequest;
import com.makotodecor.model.dto.ImgTypePagedCriteria;
import com.makotodecor.service.ImgTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImgTypeController implements ImgTypeServiceApi {

  private final ImgTypeService imgTypeService;

  @Override
  public ResponseEntity<List<ImgTypeResponse>> _getAllImgTypes() {
    return ResponseEntity.ok(imgTypeService.getAllImgTypes());
  }

  public ResponseEntity<ImgTypesPagedResponse> _getImgTypesPaged(Integer page, Integer size, String orderBy,
      String name, String code, String status) {
    var criteria = ImgTypePagedCriteria.builder()
        .page(page)
        .size(size)
        .orderBy(orderBy)
        .name(name)
        .code(code)
        .status(status)
        .build();
    return ResponseEntity.ok(imgTypeService.getImgTypesPaged(criteria));
  }

  @Override
  public ResponseEntity<ImgTypeResponse> _getImgType(Long imgTypeId) {
    return ResponseEntity.ok(imgTypeService.getImgType(imgTypeId));
  }

  @Override
  // @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<ImgTypeResponse> _createImgType(CreateImgTypeRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(imgTypeService.createImgType(request));
  }

  @Override
  // @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<ImgTypeResponse> _updateImgType(Long imgTypeId, UpdateImgTypeRequest request) {
    return ResponseEntity.ok(imgTypeService.updateImgType(imgTypeId, request));
  }

  @Override
  // @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<String> _updateImgTypesStatus(UpdateImgTypesStatusRequest request) {
    imgTypeService.updateImgTypesStatus(request);
    return ResponseEntity.ok("Image types status updated successfully");
  }

  @Override
  // @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> _deleteImgType(Long imgTypeId) {
    imgTypeService.deleteImgType(imgTypeId);
    return ResponseEntity.ok("Image type deleted successfully");
  }
}
