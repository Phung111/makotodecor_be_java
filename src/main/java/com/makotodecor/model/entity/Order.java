package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
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

  @Type(OrderStatusPsqlConvertedType.class)
  private OrderStatusEnum status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderItem> orderItems;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Column(name = "updated_by")
  private Long updatedBy;
}
