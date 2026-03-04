package com.sprint.mission.discodeit.base;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
public abstract class BaseEntity {

  protected final UUID id;
  @CreatedDate
  protected Instant createdAt;

  public BaseEntity() {
    this.id = UUID.randomUUID();
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
    return id.equals(((BaseEntity) obj).id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}