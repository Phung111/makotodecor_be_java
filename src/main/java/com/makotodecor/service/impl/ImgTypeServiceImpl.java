package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.ImgTypeMapper;
import com.makotodecor.model.CreateImgTypeRequest;
import com.makotodecor.model.ImgTypeResponse;
import com.makotodecor.model.UpdateImgTypeRequest;
import com.makotodecor.model.UpdateImgTypesStatusRequest;
import com.makotodecor.model.entity.ImgType;
import com.makotodecor.model.enums.ImgTypeStatusEnum;
import com.makotodecor.repository.ImgTypeRepository;
import com.makotodecor.service.ImgTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImgTypeServiceImpl implements ImgTypeService {

  private final ImgTypeRepository imgTypeRepository;
  private final ImgTypeMapper imgTypeMapper;

  @Override
  public List<ImgTypeResponse> getAllImgTypes() {
    log.debug("Fetching all image types");

    return imgTypeRepository.findAll()
        .stream()
        .map(imgTypeMapper::toImgTypeResponse)
        .toList();
  }

  @Override
  public ImgTypeResponse getImgType(Long imgTypeId) {
    ImgType imgType = findImgTypeById(imgTypeId);
    return imgTypeMapper.toImgTypeResponse(imgType);
  }

  @Override
  @Transactional
  public ImgTypeResponse createImgType(CreateImgTypeRequest request) {
    // Check if code already exists
    if (imgTypeRepository.existsByCode(request.getCode())) {
      throw new WebBadRequestException(ErrorMessage.IMGTYPE_CODE_ALREADY_EXISTS);
    }

    ImgType imgType = imgTypeMapper.toImgType(request);
    imgType.setCreatedAt(ZonedDateTime.now());

    imgType = imgTypeRepository.save(imgType);

    return imgTypeMapper.toImgTypeResponse(imgType);
  }

  @Override
  @Transactional
  public ImgTypeResponse updateImgType(Long imgTypeId, UpdateImgTypeRequest request) {
    ImgType imgType = findImgTypeById(imgTypeId);

    // Check if code already exists for different imgType
    if (request.getCode() != null && !request.getCode().equals(imgType.getCode())) {
      if (imgTypeRepository.existsByCode(request.getCode())) {
        throw new WebBadRequestException(ErrorMessage.IMGTYPE_CODE_ALREADY_EXISTS);
      }
    }

    imgType = imgTypeMapper.updateImgTypeFromRequest(request, imgType);

    imgType = imgTypeRepository.save(imgType);

    return imgTypeMapper.toImgTypeResponse(imgType);
  }

  @Override
  @Transactional
  public void updateImgTypesStatus(UpdateImgTypesStatusRequest request) {
    var imgTypes = imgTypeRepository.findAllById(request.getImgTypeIds());

    if (imgTypes.size() != request.getImgTypeIds().size()) {
      throw new WebBadRequestException(ErrorMessage.IMGTYPE_NOT_FOUND);
    }

    var newStatus = ImgTypeStatusEnum.valueOf(request.getStatus().getValue());
    imgTypes.forEach(imgType -> {
      imgType.setStatus(newStatus);
      imgType.setUpdatedAt(ZonedDateTime.now());
    });

    imgTypeRepository.saveAll(imgTypes);
  }

  @Override
  @Transactional
  public void deleteImgType(Long imgTypeId) {
    ImgType imgType = findImgTypeById(imgTypeId);

    // Check if imgType has images
    if (imgType.getImgs() != null && !imgType.getImgs().isEmpty()) {
      throw new WebBadRequestException(ErrorMessage.IMGTYPE_HAS_IMAGES);
    }

    imgTypeRepository.delete(imgType);
  }

  private ImgType findImgTypeById(Long imgTypeId) {
    return imgTypeRepository.findById(imgTypeId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.IMGTYPE_NOT_FOUND));
  }
}
