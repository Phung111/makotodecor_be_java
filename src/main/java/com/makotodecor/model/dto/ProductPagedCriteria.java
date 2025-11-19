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
  private String name;
  private Integer minPrice;
  private Integer maxPrice;
  private String status;
  private String category;
}
