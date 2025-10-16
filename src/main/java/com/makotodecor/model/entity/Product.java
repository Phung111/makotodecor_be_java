package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.makotodecor.model.enums.ProductStatusEnum;
import com.makotodecor.model.type.ProductStatusPsqlConvertedType;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Type(ProductStatusPsqlConvertedType.class)
  private ProductStatusEnum status;

  @Column()
  private String description;

  @Column(nullable = false)
  private Long discount;

  @Column(nullable = false)
  private Long sold;

  @Column(name = "base_sold", nullable = false)
  private Long baseSold;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  private List<Color> colors;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  private List<Size> sizes;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  private List<Img> imgs;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Column(name = "updated_by")
  private Long updatedBy;
}
