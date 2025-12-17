package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "imgs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Img {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long priority;

  @Column(nullable = false)
  private String url;

  @Column(name = "public_id")
  private String publicId;

  private String title;

  private String subtitle;

  @Column(name = "is_default")
  @Builder.Default
  private Boolean isDefault = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "img_type_id")
  private ImgType imgType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_group_id")
  private OrderGroup orderGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_item_id")
  private OrderItem orderItem;

  @Column(name = "created_at", nullable = false)
  @CreatedDate
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private ZonedDateTime updatedAt;
}
