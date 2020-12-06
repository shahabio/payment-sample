package io.shahab.paymentservice.commons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Persistable<String> {

  @Id
  private String id;

  @Embedded
  @AttributeOverride(name = "epoch", column = @Column(name = "createdDate", nullable = false))
  private EpochDateTime createdDate;

  @Embedded
  @AttributeOverride(name = "epoch", column = @Column(name = "updateDate", nullable = false))
  private EpochDateTime updatedDate;

  @JsonIgnore
  @Transient
  private boolean isNew = true;

  @JsonIgnore
  @Override
  public boolean isNew() {
    return isNew;
  }

  @PrePersist
  protected void prePersist() {
    markNotNew();
    generateId();
    putCreatedDate();
    putUpdatedDate();
  }

  @PreUpdate
  protected void preUpdate() {
    putUpdatedDate();
  }

  private void generateId() {
    if (id == null)
      id = UUID.randomUUID().toString();
  }

  private void putCreatedDate() {
    if (createdDate == null)
      createdDate = EpochDateTime.now();
  }

  private void putUpdatedDate() {
    updatedDate = EpochDateTime.now();
  }

  @PostLoad
  private void markNotNew() {
    isNew = false;
  }

}

