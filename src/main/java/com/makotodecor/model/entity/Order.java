package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.makotodecor.model.enums.OrderStatusEnum;
import com.makotodecor.model.type.OrderStatusPsqlConvertedType;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Type(OrderStatusPsqlConvertedType.class)
  private OrderStatusEnum status;

  @Column(name = "shipping_full_name")
  private String shippingFullName;

  @Column(name = "shipping_phone")
  private String shippingPhone;

  @Column(name = "shipping_address")
  private String shippingAddress;

  @Column(name = "shipping_note")
  private String shippingNote;

  @Column(name = "payment_proof_url")
  private String paymentProofUrl;

  @Column(name = "payment_proof_public_id")
  private String paymentProofPublicId;

  @Column(name = "total_price")
  private Long totalPrice;

  @Column(name = "product_count")
  private Long productCount;

  @Column(name = "total_quantity")
  private Long totalQuantity;

  @Column(name = "deposit_amount")
  private Long depositAmount;

  @Column(name = "remaining_amount")
  private Long remainingAmount;

  @Column(name = "shipping_facebook_link")
  private String shippingFacebookLink;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderItem> orderItems;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderGroup> orderGroups;

  @Column(name = "created_at", nullable = false)
  @CreatedDate
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private ZonedDateTime updatedAt;

  @Column(name = "updated_by")
  private Long updatedBy;
}
