package com.makotodecor.service;

import com.makotodecor.model.CreateImgRequest;
import com.makotodecor.model.ImgDetailResponse;
import com.makotodecor.model.ImgsPagedResponse;
import com.makotodecor.model.UpdateImgRequest;
import com.makotodecor.model.dto.ImgPagedCriteria;

public interface ImgService {

  ImgsPagedResponse getImgsPaged(ImgPagedCriteria criteria);

  ImgDetailResponse getImg(Long imgId);

  ImgDetailResponse createImg(CreateImgRequest request);

  ImgDetailResponse updateImg(Long imgId, UpdateImgRequest request);

  void deleteImg(Long imgId);
}

