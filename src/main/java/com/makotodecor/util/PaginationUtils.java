package com.makotodecor.util;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.model.PageInfo;

public class PaginationUtils {

  public static PageInfo toPageInfo(Page<?> page) {
    PageInfo metadata = new PageInfo();
    metadata.setTotalElements(page.getTotalElements());
    metadata.setTotalPages(page.getTotalPages());
    metadata.setCurrentPage(page.getNumber());
    metadata.setPageSize(page.getSize());
    metadata.setIsFirst(page.isFirst());
    metadata.setIsLast(page.isLast());
    return metadata;
  }

  public static Sort parseSortCriteria(String sortCriteria) {
    if (sortCriteria == null || sortCriteria.isBlank()) {
      sortCriteria = "updatedAt,desc";
    }
    String[] sortParams = sortCriteria.split(",");
    if (sortParams.length % 2 != 0) {
      throw new IllegalArgumentException(
          "Invalid sort criteria format. Expected format: 'column1,asc,column2,desc'");
    }
    Sort sort = Sort.unsorted();
    for (int i = 0; i < sortParams.length; i += 2) {
      String column = sortParams[i];
      Sort.Direction direction = Sort.Direction.fromString(sortParams[i + 1]);
      sort = sort.and(Sort.by(direction, column));
    }
    return sort;
  }

  public static void validateSortColumns(Sort sort, Set<String> sortableColumns) {
    sort.forEach(order -> {
      String property = order.getProperty();
      if (!sortableColumns.contains(property)) {
        throw new WebBadRequestException(ErrorMessage.UTILS_SORTABLE_COLUMNS_DOES_NOT_CONTAIN_VALUE);
      }
    });
  }
}
