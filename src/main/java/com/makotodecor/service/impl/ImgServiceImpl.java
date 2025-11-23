package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.ImgMapper;
import com.makotodecor.model.CreateImgRequest;
import com.makotodecor.model.ImgDetailResponse;
import com.makotodecor.model.ImgsPagedResponse;
import com.makotodecor.model.UpdateImgRequest;
import com.makotodecor.model.dto.ImgPagedCriteria;
import com.makotodecor.model.entity.Img;
import com.makotodecor.model.entity.ImgType;
import com.makotodecor.model.entity.Product;
import com.makotodecor.repository.ImgRepository;
import com.makotodecor.repository.ImgTypeRepository;
import com.makotodecor.repository.ProductRepository;
import com.makotodecor.service.ImgService;
import com.makotodecor.util.PaginationUtils;
import com.makotodecor.util.QuerydslCriteriaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImgServiceImpl implements ImgService {

  private final ImgRepository imgRepository;
  private final ImgMapper imgMapper;
  private final ImgTypeRepository imgTypeRepository;
  private final ProductRepository productRepository;

  private static final Set<String> IMG_SORTABLE_COLUMNS = Set.of("id", "priority", "url", "createdAt", "updatedAt");

  @Override
  @Transactional(readOnly = true)
  public ImgsPagedResponse getImgsPaged(ImgPagedCriteria criteria) {
    // Validate imgTypeId is required
    if (criteria.getImgTypeId() == null) {
      throw new WebBadRequestException(ErrorMessage.IMAGE_TYPE_REQUIRED);
    }

    var sortCriteria = PaginationUtils.parseSortCriteria(criteria.getOrderBy());
    PaginationUtils.validateSortColumns(sortCriteria, IMG_SORTABLE_COLUMNS);

    var pageable = PageRequest
        .of(criteria.getPage(), criteria.getSize())
        .withSort(sortCriteria);

    var predicate = QuerydslCriteriaUtils.buildImgSearchPredicate(criteria)
        .orElse(QuerydslCriteriaUtils.truePredicate());

    var pageResponse = imgRepository.findAll(predicate, pageable);

    // Eager load relationships
    var imgs = pageResponse.getContent();
    imgs.forEach(img -> {
      if (img.getProduct() != null) {
        img.getProduct().getName();
      }
      if (img.getImgType() != null) {
        img.getImgType().getName();
      }
    });

    return ImgsPagedResponse.builder()
        .pageInfo(PaginationUtils.toPageInfo(pageResponse))
        .items(imgs.stream()
            .map(imgMapper::toImgItemResponse)
            .toList())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public ImgDetailResponse getImg(Long imgId) {
    Img img = findImgById(imgId);
    
    // Eager load relationships
    if (img.getProduct() != null) {
      img.getProduct().getName();
    }
    if (img.getImgType() != null) {
      img.getImgType().getName();
    }

    return imgMapper.toImgDetailResponse(img);
  }

  @Override
  @Transactional
  public ImgDetailResponse createImg(CreateImgRequest request) {
    Img img = imgMapper.toImg(request);

    // Set imgType
    if (request.getImgTypeId() != null) {
      ImgType imgType = imgTypeRepository.findById(request.getImgTypeId())
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.IMAGE_TYPE_NOT_FOUND));
      img.setImgType(imgType);
    }

    // Set product if provided
    if (request.getProductId() != null) {
      Product product = productRepository.findById(request.getProductId())
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));
      img.setProduct(product);
    }

    img = imgRepository.save(img);
    return imgMapper.toImgDetailResponse(img);
  }

  @Override
  @Transactional
  public ImgDetailResponse updateImg(Long imgId, UpdateImgRequest request) {
    Img img = findImgById(imgId);

    img = imgMapper.updateImgFromRequest(request, img);

    // Update imgType if provided
    if (request.getImgTypeId() != null) {
      ImgType imgType = imgTypeRepository.findById(request.getImgTypeId())
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.IMAGE_TYPE_NOT_FOUND));
      img.setImgType(imgType);
    }

    // Update product if provided
    if (request.getProductId() != null) {
      Product product = productRepository.findById(request.getProductId())
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));
      img.setProduct(product);
    }

    img = imgRepository.save(img);
    return imgMapper.toImgDetailResponse(img);
  }

  @Override
  @Transactional
  public void deleteImg(Long imgId) {
    Img img = findImgById(imgId);
    imgRepository.delete(img);
  }

  private Img findImgById(Long imgId) {
    return imgRepository.findById(imgId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.IMAGE_NOT_FOUND));
  }
}

