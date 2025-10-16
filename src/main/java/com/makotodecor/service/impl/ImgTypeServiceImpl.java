package com.makotodecor.service.impl;

import com.makotodecor.model.ImgTypeResponse;
import com.makotodecor.repository.ImgTypeRepository;
import com.makotodecor.service.ImgTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImgTypeServiceImpl implements ImgTypeService {

  private final ImgTypeRepository imgTypeRepository;

  @Override
  public List<ImgTypeResponse> getAllImgTypes() {
    log.debug("Fetching all image types");

    return imgTypeRepository.findAll()
        .stream()
        .map(imgType -> ImgTypeResponse.builder()
            .id(imgType.getId())
            .name(imgType.getName())
            .code(imgType.getCode())
            .status(imgType.getStatus() != null
                ? ImgTypeResponse.StatusEnum.fromValue(imgType.getStatus().getValue())
                : null)
            .build())
        .collect(Collectors.toList());
  }
}
