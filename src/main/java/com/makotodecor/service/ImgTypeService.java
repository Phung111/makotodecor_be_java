package com.makotodecor.service;

import com.makotodecor.model.CreateImgTypeRequest;
import com.makotodecor.model.ImgTypeResponse;
import com.makotodecor.model.UpdateImgTypeRequest;
import com.makotodecor.model.UpdateImgTypesStatusRequest;

import java.util.List;

public interface ImgTypeService {

  List<ImgTypeResponse> getAllImgTypes();

  ImgTypeResponse getImgType(Long imgTypeId);

  ImgTypeResponse createImgType(CreateImgTypeRequest request);

  ImgTypeResponse updateImgType(Long imgTypeId, UpdateImgTypeRequest request);

  void updateImgTypesStatus(UpdateImgTypesStatusRequest request);

  void deleteImgType(Long imgTypeId);
}
