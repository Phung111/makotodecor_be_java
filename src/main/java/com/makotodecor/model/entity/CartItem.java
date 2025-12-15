package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "cartItems")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;

  @Column(nullable = false)
  private Long quantity;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private Long discount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "size_id")
  private Size size;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "color_id")
  private Color color;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;
}
