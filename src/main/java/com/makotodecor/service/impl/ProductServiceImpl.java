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
import com.makotodecor.model.UpdateProductsStatusRequest;
import com.makotodecor.model.dto.ProductPagedCriteria;
import com.makotodecor.model.entity.Category;
import com.makotodecor.model.entity.Color;
import com.makotodecor.model.entity.Img;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.Size;
import com.makotodecor.model.enums.ProductStatusEnum;
import com.makotodecor.repository.CartItemRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  private final CartItemRepository cartItemRepository;

  private static final Set<String> PRODUCT_SORTABLE_COLUMNS = Set.of("id", "name", "status", "price", "discount",
      "sold", "category", "updatedAt");

  @Override
  @Transactional(readOnly = true)
  public ProductsPagedResponse getProductsPaged(ProductPagedCriteria criteria) {
    var sortCriteria = PaginationUtils.parseSortCriteria(criteria.getOrderBy());
    PaginationUtils.validateSortColumns(sortCriteria, PRODUCT_SORTABLE_COLUMNS);

    var predicate = QuerydslCriteriaUtils.buildProductSearchPredicate(criteria)
        .orElse(QuerydslCriteriaUtils.truePredicate());

    var pageable = PageRequest
        .of(criteria.getPage(), criteria.getSize())
        .withSort(sortCriteria);

    var pageResponse = productRepository.findAllWithSpecialSort(predicate, pageable);

    List<Product> products = pageResponse.getContent();
    products.forEach(product -> {
      if (product.getSizes() != null) {
        product.getSizes().size();
      }
    });

    return ProductsPagedResponse.builder()
        .pageInfo(PaginationUtils.toPageInfo(pageResponse))
        .items(products.stream()
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
        .updatedAt(ZonedDateTime.now())
        .build();

    product = productRepository.save(product);

    if (request.getPrices() != null && !request.getPrices().isEmpty()) {
      List<Size> sizes = createSizesFromPrices(request.getPrices(), product);
      sizes = sizeRepository.saveAll(sizes);
      product.setSizes(sizes);
    }

    // Save color images first, then colors (colors reference images)
    List<Color> colors = null;
    if (request.getColors() != null && !request.getColors().isEmpty()) {
      colors = createColorsFromRequest(request.getColors(), product);
      // Extract and save all images from colors first
      List<Img> colorImages = colors.stream()
          .map(Color::getImg)
          .filter(java.util.Objects::nonNull)
          .toList();
      if (!colorImages.isEmpty()) {
        imgRepository.saveAll(colorImages);
      }
      // Now save colors (with persisted img references)
      colors = colorRepository.saveAll(colors);
      product.setColors(colors);
    }

    // Create and save other images (default, other, detail)
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

    // Save other images (default, other, detail) - color images already saved above
    if (!images.isEmpty()) {
      images = imgRepository.saveAll(images);
    }

    // Add color images to the images list (already persisted, just for
    // product.getImgs())
    if (colors != null && !colors.isEmpty()) {
      List<Img> colorImages = colors.stream()
          .map(Color::getImg)
          .filter(java.util.Objects::nonNull)
          .toList();
      images.addAll(colorImages);
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

    if (request.getPrices() != null) {
      final Product currentProduct = product; // ensure effectively final inside lambdas
      // Preserve sizes that are still referenced in carts to avoid FK violations
      List<Size> existingSizes = sizeRepository.findByProductId(productId);
      Map<String, Size> existingByLabel = new HashMap<>();
      existingSizes.forEach(size -> existingByLabel.put(size.getSize(), size));

      List<Size> sizesToSave = new ArrayList<>();

      request.getPrices().forEach(priceReq -> {
        Size size = existingByLabel.remove(priceReq.getSize());
        if (size == null) {
          // New size
          sizesToSave.add(productMapper.toSize(priceReq, currentProduct));
        } else {
          // Update existing size in place to keep references intact
          // price in request is Double; convert to Long to match entity
          size.setPrice(priceReq.getPrice() != null ? priceReq.getPrice().longValue() : null);
          size.setSize(priceReq.getSize());
          size.setProduct(currentProduct);
          if (priceReq.getIsActive() != null) {
            size.setIsActive(priceReq.getIsActive());
          }
          sizesToSave.add(size);
        }
      });

      // Delete sizes that are no longer present only when not referenced in carts
      if (!existingByLabel.isEmpty()) {
        existingByLabel.values().forEach(size -> {
          if (size.getId() != null && cartItemRepository.existsBySizeId(size.getId())) {
            log.warn("Skip deleting size {} for product {} because it is referenced in cart items",
                size.getId(), productId);
          } else {
            sizeRepository.delete(size);
          }
        });
      }

      if (!sizesToSave.isEmpty()) {
        sizeRepository.saveAll(sizesToSave);
      }
    }

    // Handle colors: update in place, avoid deleting colors referenced by carts
    List<Color> colorsToPersist = new ArrayList<>();
    if (request.getColors() != null) {
      // Map existing colors by name (or id if present). Prefer id when provided.
      List<Color> existingColors = colorRepository.findByProductId(productId);
      Map<Long, Color> existingById = new HashMap<>();
      Map<String, Color> existingByName = new HashMap<>();
      existingColors.forEach(color -> {
        if (color.getId() != null) {
          existingById.put(color.getId(), color);
        }
        if (color.getName() != null) {
          existingByName.put(color.getName(), color);
        }
      });

      request.getColors().forEach(colorReq -> {
        Color existing = null;
        if (colorReq.getId() != null) {
          existing = existingById.remove(colorReq.getId());
        }
        if (existing == null && colorReq.getName() != null) {
          existing = existingByName.remove(colorReq.getName());
        }

        if (existing == null) {
          colorsToPersist.add(productMapper.toColor(colorReq, product));
        } else {
          // Update existing color fields to keep ID (and FK) intact
          existing.setName(colorReq.getName());
          existing.setColor(colorReq.getColorCode());
          existing.setProduct(product);
          if (colorReq.getIsActive() != null) {
            existing.setIsActive(colorReq.getIsActive());
          }

          // Update or replace color image
          if (colorReq.getImage() != null) {
            Img existingImg = existing.getImg();
            if (existingImg == null) {
              existing.setImg(productMapper.toImg(colorReq.getImage(), product, false, 0L));
            } else {
              existingImg.setUrl(colorReq.getImage().getUrl());
              existingImg.setPublicId(colorReq.getImage().getPublicId());
              existingImg.setImgType(
                  productMapper.createImgTypeReference(colorReq.getImage().getImgTypeId()));
              existingImg.setUpdatedAt(ZonedDateTime.now());
            }
          }

          colorsToPersist.add(existing);
        }
      });

      // Delete colors that are no longer present only if not referenced by cart items
      if (!existingById.isEmpty()) {
        existingById.values().forEach(color -> {
          if (color.getId() != null && cartItemRepository.existsByColorId(color.getId())) {
            log.warn("Skip deleting color {} for product {} because it is referenced in cart items",
                color.getId(), productId);
          } else {
            colorRepository.delete(color);
          }
        });
      }
    }

    // Delete old images for product (non-color images) BEFORE inserting new ones
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

    // Persist color images first, then colors
    if (!colorsToPersist.isEmpty()) {
      List<Img> colorImages = colorsToPersist.stream()
          .map(Color::getImg)
          .filter(java.util.Objects::nonNull)
          .toList();
      if (!colorImages.isEmpty()) {
        imgRepository.saveAll(colorImages);
      }
      colorRepository.saveAll(colorsToPersist);
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

    final Product finalProduct = productRepository.save(product);
    return productMapper.toProductDetailResponse(finalProduct);
  }

  @Override
  @Transactional
  public void updateProductsStatus(UpdateProductsStatusRequest request) {
    var products = productRepository.findAllById(request.getProductIds());

    if (products.size() != request.getProductIds().size()) {
      throw new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND);
    }

    var newStatus = ProductStatusEnum.valueOf(request.getStatus().getValue());
    products.forEach(product -> {
      product.setStatus(newStatus);
    });

    productRepository.saveAll(products);
  }

  private Product findProductById(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));
  }

  private List<Size> createSizesFromPrices(List<ProductPriceRequest> prices, Product product) {
    return prices.stream()
        .map(priceReq -> productMapper.toSize(priceReq, product))
        .toList();
  }

  private List<Color> createColorsFromRequest(List<ProductColorRequest> colorRequests, Product product) {
    return colorRequests.stream()
        .map(colorReq -> productMapper.toColor(colorReq, product))
        .toList();
  }

  private Img createImg(ImageInfo imageInfo, Product product, boolean isDefault, Long priority) {
    return productMapper.toImg(imageInfo, product, isDefault, priority);
  }
}
