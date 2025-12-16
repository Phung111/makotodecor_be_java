package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "colors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Color {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String color;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "img_id")
  private Img img;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isActive = true;
}
