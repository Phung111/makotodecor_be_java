package com.makotodecor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.makotodecor.model.enums.ImgTypeStatusEnum;
import com.makotodecor.model.type.ImgTypeStatusPsqlConvertedType;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "img_types")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class ImgType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String code;

  @Type(ImgTypeStatusPsqlConvertedType.class)
  private ImgTypeStatusEnum status;

  @OneToMany(mappedBy = "imgType", fetch = FetchType.LAZY)
  private List<Img> imgs;

  @Column(name = "created_at", nullable = false)
  @CreatedDate
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private ZonedDateTime updatedAt;

  @Column(name = "updated_by")
  private Long updatedBy;
}
