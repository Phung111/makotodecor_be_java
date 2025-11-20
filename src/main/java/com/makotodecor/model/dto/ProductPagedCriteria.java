package com.makotodecor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPagedCriteria {
  private Integer page;
  private Integer size;
  private String orderBy;
  private String keySearch;
  private Long minPrice;
  private Long maxPrice;
  private String status;
  private Long categoryId;
}
