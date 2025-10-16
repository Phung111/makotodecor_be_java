package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.makotodecor.model.enums.UserStatusEnum;
import com.makotodecor.model.enums.RoleEnum;
import com.makotodecor.model.type.UserStatusPsqlConvertedType;
import com.makotodecor.model.type.RolePsqlConvertedType;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  private String phone;

  @Type(UserStatusPsqlConvertedType.class)
  private UserStatusEnum status;

  @Type(RolePsqlConvertedType.class)
  @Column(nullable = false)
  private RoleEnum role = RoleEnum.USER;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private Cart cart;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Order> orders;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;
}
