package com.makotodecor.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.openapitools.jackson.nullable.JsonNullable;

import com.makotodecor.model.ImageInfo;
import com.makotodecor.model.ProductColorRequest;
import com.makotodecor.model.ProductItemResponse;
import com.makotodecor.model.ProductPriceRequest;
import com.makotodecor.model.ProductDetailResponse;
import com.makotodecor.model.entity.Color;
import com.makotodecor.model.entity.Img;
import com.makotodecor.model.entity.ImgType;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.Size;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(target = "img", expression = "java(getDefaultImageUrl(product))")
  @Mapping(target = "minPrice", expression = "java(getMinPrice(product))")
  @Mapping(target = "finalPrice", expression = "java(getFinalPrice(product))")
  @Mapping(target = "sold", expression = "java(getTotalSold(product))")
  @Mapping(target = "sizeId", expression = "java(toJsonNullable(getMinPriceSizeIdValue(product)))")
  @Mapping(target = "colorId", expression = "java(toJsonNullable(getFirstColorIdValue(product)))")
  ProductItemResponse toProductItemResponse(Product product);

  @Mapping(target = "status", expression = "java(mapStatusToDto(product.getStatus()))")
  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "discount", source = "discount")
  @Mapping(target = "baseSold", source = "baseSold")
  @Mapping(target = "defaultImage", expression = "java(getDefaultImage(product))")
  @Mapping(target = "prices", expression = "java(mapSizesToPrices(product.getSizes()))")
  @Mapping(target = "colors", expression = "java(mapColorsToResponse(product.getColors()))")
  @Mapping(target = "otherImages", expression = "java(getOtherImages(product))")
  @Mapping(target = "detailImages", expression = "java(getDetailImages(product))")
  ProductDetailResponse toProductDetailResponse(Product product);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "size", source = "request.size")
  @Mapping(target = "price", expression = "java(request.getPrice().longValue())")
  @Mapping(target = "product", source = "product")
  Size toSize(ProductPriceRequest request, Product product);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "color", source = "request.colorCode")
  @Mapping(target = "img", expression = "java(toColorImg(request.getImage(), product))")
  @Mapping(target = "product", source = "product")
  Color toColor(ProductColorRequest request, Product product);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "url", source = "imageInfo.url")
  @Mapping(target = "publicId", source = "imageInfo.publicId")
  @Mapping(target = "isDefault", source = "isDefault")
  @Mapping(target = "priority", source = "priority")
  @Mapping(target = "title", ignore = true)
  @Mapping(target = "subtitle", ignore = true)
  @Mapping(target = "product", source = "product")
  @Mapping(target = "imgType", expression = "java(createImgTypeReference(imageInfo.getImgTypeId()))")
  Img toImg(ImageInfo imageInfo, Product product, boolean isDefault, Long priority);

  default ProductPriceRequest toProductPriceRequest(Size size) {
    if (size == null) {
      return null;
    }
    ProductPriceRequest request = new ProductPriceRequest();
    request.setId(size.getId());
    request.setSize(size.getSize());
    request.setPrice(size.getPrice() != null ? size.getPrice().doubleValue() : null);
    return request;
  }

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "colorCode", source = "color")
  @Mapping(target = "image", expression = "java(mapColorImage(color))")
  ProductColorRequest toProductColorRequest(Color color);

  @Mapping(target = "url", source = "url")
  @Mapping(target = "publicId", source = "publicId")
  @Mapping(target = "typeCode", source = "imgType.code")
  @Mapping(target = "imgTypeId", source = "imgType.id")
  ImageInfo toImageInfo(Img img);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "colorCode", source = "color")
  @Mapping(target = "image", expression = "java(mapColorImage(color))")
  com.makotodecor.model.ProductColorResponse toProductColorResponse(Color color);

  default com.makotodecor.model.ProductPriceResponse toProductPriceResponse(Size size) {
    if (size == null) {
      return null;
    }
    com.makotodecor.model.ProductPriceResponse response = new com.makotodecor.model.ProductPriceResponse();
    response.setId(size.getId());
    response.setSize(size.getSize());
    response.setPrice(size.getPrice() != null ? size.getPrice() : null);
    return response;
  }

  default String getDefaultImageUrl(Product product) {
    if (product == null || product.getImgs() == null || product.getImgs().isEmpty()) {
      return null;
    }

    return product.getImgs()
        .stream()
        .sorted((a, b) -> {
          boolean ad = Boolean.TRUE.equals(a.getIsDefault());
          boolean bd = Boolean.TRUE.equals(b.getIsDefault());
          if (ad != bd) {
            return ad ? -1 : 1;
          }
          return Comparator.comparing(Img::getPriority).compare(a, b);
        })
        .map(Img::getUrl)
        .findFirst()
        .orElse(null);
  }

  default com.makotodecor.model.ProductStatusEnum mapStatusToDto(
      com.makotodecor.model.enums.ProductStatusEnum status) {
    if (status == null) {
      return null;
    }
    return com.makotodecor.model.ProductStatusEnum.valueOf(status.name());
  }

  default ImageInfo mapColorImage(Color color) {
    if (color == null || color.getImg() == null) {
      return null;
    }
    return toImageInfo(color.getImg());
  }

  default Img toColorImg(ImageInfo imageInfo, Product product) {
    if (imageInfo == null) {
      return null;
    }
    return toImg(imageInfo, product, false, 0L);
  }

  default ImgType createImgTypeReference(Long imgTypeId) {
    if (imgTypeId == null) {
      return null;
    }
    ImgType imgType = new ImgType();
    imgType.setId(imgTypeId);
    return imgType;
  }

  default ImageInfo getDefaultImage(Product product) {
    if (product == null || product.getImgs() == null) {
      return null;
    }
    return product.getImgs().stream()
        .filter(img -> Boolean.TRUE.equals(img.getIsDefault()))
        .findFirst()
        .map(this::toImageInfo)
        .orElse(null);
  }

  default List<ImageInfo> getOtherImages(Product product) {
    if (product == null || product.getImgs() == null) {
      return new ArrayList<>();
    }
    return product.getImgs().stream()
        .filter(img -> img.getImgType() != null && "OTHER".equals(img.getImgType().getCode()))
        .map(this::toImageInfo)
        .toList();
  }

  default List<ImageInfo> getDetailImages(Product product) {
    if (product == null || product.getImgs() == null) {
      return new ArrayList<>();
    }
    return product.getImgs().stream()
        .filter(img -> img.getImgType() != null && "DETAIL".equals(img.getImgType().getCode()))
        .map(this::toImageInfo)
        .toList();
  }

  default List<com.makotodecor.model.ProductPriceResponse> mapSizesToPrices(List<Size> sizes) {
    if (sizes == null) {
      return new ArrayList<>();
    }
    return sizes.stream()
        .map(this::toProductPriceResponse)
        .filter(Objects::nonNull)
        .toList();
  }

  default List<com.makotodecor.model.ProductColorResponse> mapColorsToResponse(List<Color> colors) {
    if (colors == null) {
      return new ArrayList<>();
    }
    return colors.stream()
        .map(this::toProductColorResponse)
        .filter(Objects::nonNull)
        .toList();
  }

  default Long getMinPrice(Product product) {
    if (product == null || product.getSizes() == null || product.getSizes().isEmpty()) {
      return null;
    }
    return product.getSizes().stream()
        .map(Size::getPrice)
        .filter(Objects::nonNull)
        .min(Long::compare)
        .orElse(null);
  }

  default Long getFinalPrice(Product product) {
    Long minPrice = getMinPrice(product);
    if (minPrice == null || product.getDiscount() == null) {
      return null;
    }
    // Calculate: minPrice * (100 - discount) / 100
    return minPrice * (100 - product.getDiscount()) / 100;
  }

  default Long getTotalSold(Product product) {
    if (product == null) {
      return null;
    }
    Long sold = product.getSold() == null ? 0L : product.getSold();
    Long baseSold = product.getBaseSold() == null ? 0L : product.getBaseSold();
    return sold + baseSold;
  }

  default Long getMinPriceSizeIdValue(Product product) {
    if (product == null || product.getSizes() == null || product.getSizes().isEmpty()) {
      return null;
    }
    return product.getSizes().stream()
        .filter(Objects::nonNull)
        .filter(size -> size.getPrice() != null)
        .min(Comparator.comparing(Size::getPrice))
        .map(Size::getId)
        .orElse(null);
  }

  default Long getFirstColorIdValue(Product product) {
    if (product == null || product.getColors() == null || product.getColors().isEmpty()) {
      return null;
    }
    return product.getColors().stream()
        .filter(Objects::nonNull)
        .map(Color::getId)
        .findFirst()
        .orElse(null);
  }

  default JsonNullable<Long> toJsonNullable(Long value) {
    return value != null ? JsonNullable.of(value) : JsonNullable.undefined();
  }

  @AfterMapping
  default void extractJsonNullableValues(@MappingTarget com.makotodecor.model.ProductItemResponse response) {
    if (response.getSizeId() != null && response.getSizeId().isPresent()) {
      response.setSizeId(JsonNullable.of(response.getSizeId().get()));
    } else {
      response.setSizeId(JsonNullable.undefined());
    }
    if (response.getColorId() != null && response.getColorId().isPresent()) {
      response.setColorId(JsonNullable.of(response.getColorId().get()));
    } else {
      response.setColorId(JsonNullable.undefined());
    }
  }
}
