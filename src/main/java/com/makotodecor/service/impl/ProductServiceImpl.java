package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.ProductMapper;
import com.makotodecor.model.CreateProductRequest;
import com.makotodecor.model.ImageInfo;
import com.makotodecor.model.ProductColorRequest;
import com.makotodecor.model.ProductDetailResponse;
import com.makotodecor.model.ProductPriceRequest;
import com.makotodecor.model.ProductsPagedResponse;
import com.makotodecor.model.UpdateProductRequest;
import com.makotodecor.model.dto.ProductPagedCriteria;
import com.makotodecor.model.entity.Category;
import com.makotodecor.model.entity.Color;
import com.makotodecor.model.entity.Img;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.Size;
import com.makotodecor.model.enums.ProductStatusEnum;
import com.makotodecor.repository.CategoryRepository;
import com.makotodecor.repository.ColorRepository;
import com.makotodecor.repository.ImgRepository;
import com.makotodecor.repository.ProductRepository;
import com.makotodecor.repository.SizeRepository;
import com.makotodecor.service.FileUploadService;
import com.makotodecor.service.ProductService;
import com.makotodecor.util.PaginationUtils;
import com.makotodecor.util.ProductValidationUtils;
import com.makotodecor.util.QuerydslCriteriaUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ColorRepository colorRepository;
  private final SizeRepository sizeRepository;
  private final ImgRepository imgRepository;
  private final FileUploadService fileUploadService;
  private final ProductMapper productMapper;

  private static final Set<String> PRODUCT_SORTABLE_COLUMNS = Set.of("id", "name", "status", "discount", "sold",
      "category", "updatedAt");

  @Override
  public ProductsPagedResponse getProductsPaged(ProductPagedCriteria criteria) {
    var sortCriteria = PaginationUtils.parseSortCriteria(criteria.getOrderBy());
    PaginationUtils.validateSortColumns(sortCriteria, PRODUCT_SORTABLE_COLUMNS);

    var pageable = PageRequest
        .of(criteria.getPage(), criteria.getSize())
        .withSort(sortCriteria);

    var predicate = QuerydslCriteriaUtils.buildProductSearchPredicate(criteria)
        .orElse(QuerydslCriteriaUtils.truePredicate());

    var pageResponse = productRepository.findAll(predicate, pageable);

    return ProductsPagedResponse.builder()
        .pageInfo(PaginationUtils.toPageInfo(pageResponse))
        .items(pageResponse.getContent().stream()
            .map(productMapper::toProductItemResponse)
            .toList())
        .build();
  }

  @Override
  public ProductDetailResponse getProduct(Long productId) {
    Product product = findProductById(productId);
    return productMapper.toProductDetailResponse(product);
  }

  @Override
  @Transactional
  public ProductDetailResponse createProduct(CreateProductRequest request) {

    ProductValidationUtils.validateCreateProductRequest(request);

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.CATEGORY_NOT_FOUND));

    Product product = Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .status(ProductStatusEnum.valueOf(request.getStatus().getValue()))
        .category(category)
        .discount(request.getDiscount() != null ? request.getDiscount() : 0L)
        .sold(0L)
        .baseSold(request.getBaseSold() != null ? request.getBaseSold() : 0L)
        .createdAt(ZonedDateTime.now())
        .build();

    product = productRepository.save(product);

    if (request.getPrices() != null && !request.getPrices().isEmpty()) {
      List<Size> sizes = createSizesFromPrices(request.getPrices(), product);
      product.setSizes(sizes);
    }

    if (request.getColors() != null && !request.getColors().isEmpty()) {
      List<Color> colors = createColorsFromRequest(request.getColors(), product);
      product.setColors(colors);
    }

    List<Img> images = new ArrayList<>();

    if (request.getDefaultImage() != null) {
      images.add(createImg(request.getDefaultImage(), product, true, 0L));
    }

    if (request.getOtherImages() != null && !request.getOtherImages().isEmpty()) {
      for (int i = 0; i < request.getOtherImages().size(); i++) {
        images.add(createImg(request.getOtherImages().get(i), product, false, (long) (i + 1)));
      }
    }

    if (request.getDetailImages() != null && !request.getDetailImages().isEmpty()) {
      for (int i = 0; i < request.getDetailImages().size(); i++) {
        images.add(createImg(request.getDetailImages().get(i), product, false, (long) (i + 1)));
      }
    }

    product.setImgs(images);

    product = productRepository.save(product);

    return productMapper.toProductDetailResponse(product);
  }

  @Override
  @Transactional
  public ProductDetailResponse updateProduct(Long productId, UpdateProductRequest request) {
    ProductValidationUtils.validateUpdateProductRequest(request);

    Product product = findProductById(productId);

    if (!product.getCategory().getId().equals(request.getCategoryId())) {
      Category category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.CATEGORY_NOT_FOUND));
      product.setCategory(category);
    }

    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setStatus(ProductStatusEnum.valueOf(request.getStatus().getValue()));
    if (request.getDiscount() != null) {
      product.setDiscount(request.getDiscount());
    }
    if (request.getBaseSold() != null) {
      product.setBaseSold(request.getBaseSold());
    }
    product.setUpdatedAt(ZonedDateTime.now());

    if (request.getPrices() != null) {
      sizeRepository.deleteByProductId(productId);
      List<Size> sizes = createSizesFromPrices(request.getPrices(), product);
      sizeRepository.saveAll(sizes);
    }

    if (request.getColors() != null) {
      colorRepository.deleteByProductId(productId);
      List<Color> colors = createColorsFromRequest(request.getColors(), product);
      colorRepository.saveAll(colors);
    }

    List<Img> oldImages = imgRepository.findByProductId(productId);
    if (!oldImages.isEmpty()) {
      List<String> publicIds = oldImages.stream()
          .map(Img::getPublicId)
          .filter(publicId -> publicId != null && !publicId.isEmpty())
          .toList();

      if (!publicIds.isEmpty()) {
        try {
          fileUploadService.deleteImages(publicIds);
          log.info("Deleted {} images from Cloudinary for product {}", publicIds.size(), productId);
        } catch (Exception e) {
          log.error("Failed to delete images from Cloudinary for product {}: {}", productId, e.getMessage());
        }
      }

      imgRepository.deleteByProductId(productId);
    }

    List<Img> images = new ArrayList<>();
    if (request.getDefaultImage() != null) {
      images.add(createImg(request.getDefaultImage(), product, true, 0L));
    }

    if (request.getOtherImages() != null && !request.getOtherImages().isEmpty()) {
      for (int i = 0; i < request.getOtherImages().size(); i++) {
        images.add(createImg(request.getOtherImages().get(i), product, false, (long) (i + 1)));
      }
    }

    if (request.getDetailImages() != null && !request.getDetailImages().isEmpty()) {
      for (int i = 0; i < request.getDetailImages().size(); i++) {
        images.add(createImg(request.getDetailImages().get(i), product, false, (long) (i + 1)));
      }
    }

    if (!images.isEmpty()) {
      imgRepository.saveAll(images);
    }

    product = productRepository.save(product);

    return productMapper.toProductDetailResponse(product);
  }

  private Product findProductById(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));
  }

  private List<Size> createSizesFromPrices(List<ProductPriceRequest> prices, Product product) {
    return prices.stream()
        .map(priceReq -> productMapper.toSize(priceReq, product))
        .collect(Collectors.toList());
  }

  private List<Color> createColorsFromRequest(List<ProductColorRequest> colorRequests, Product product) {
    return colorRequests.stream()
        .map(colorReq -> productMapper.toColor(colorReq, product))
        .collect(Collectors.toList());
  }

  private Img createImg(ImageInfo imageInfo, Product product, boolean isDefault, Long priority) {
    return productMapper.toImg(imageInfo, product, isDefault, priority);
  }
}
