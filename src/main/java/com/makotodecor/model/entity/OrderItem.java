package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Table(name = "orderItems")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_group_id")
  private OrderGroup orderGroup;

  @Column(nullable = false)
  private Long quantity;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private Long discount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "color_name")
  private String colorName;

  @Column(name = "size_name")
  private String sizeName;

  @Column(name = "size_price")
  private Long sizePrice;

  /**
   * Variant images snapshot stored as Img entities with type ORDER_ITEM.
   */
  @OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Img> variantImages;
}
