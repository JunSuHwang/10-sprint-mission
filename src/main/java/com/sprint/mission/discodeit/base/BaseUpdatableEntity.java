package com.sprint.mission.discodeit.base;

import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {

  protected Instant updatedAt;

  public BaseUpdatableEntity() {
    this.updatedAt = this.createdAt;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return id.equals(((BaseUpdatableEntity) obj).id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}