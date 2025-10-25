package com.makotodecor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPagedCriteria {
  private Integer page;
  private Integer size;
  private String orderBy;
  private String name;
  private String status;
}