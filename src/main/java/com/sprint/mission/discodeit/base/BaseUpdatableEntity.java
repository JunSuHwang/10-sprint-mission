package com.sprint.mission.discodeit.base;

import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  protected Instant updatedAt;

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