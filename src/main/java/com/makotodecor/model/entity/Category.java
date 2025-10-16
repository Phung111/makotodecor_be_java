package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.makotodecor.model.enums.CategoryStatusEnum;
import com.makotodecor.model.type.CategoryStatusPsqlConvertedType;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  @Type(CategoryStatusPsqlConvertedType.class)
  private CategoryStatusEnum status;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Product> products;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Column(name = "updated_by")
  private Long updatedBy;
}
