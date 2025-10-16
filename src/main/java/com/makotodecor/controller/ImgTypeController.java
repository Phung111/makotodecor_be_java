package com.makotodecor.controller;

import com.makotodecor.model.ImgTypeResponse;
import com.makotodecor.service.ImgTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
