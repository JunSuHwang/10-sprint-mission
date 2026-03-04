package com.sprint.mission.discodeit.readstatus.entity;

import com.sprint.mission.discodeit.base.BaseUpdatableEntity;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus extends BaseUpdatableEntity {

  private final UUID userId;
  private final UUID channelId;
  private Instant lastReadAt;

  public ReadStatus(UUID userId, UUID channelId) {
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = Instant.now();
  }

  public void update(Instant newLastReadAt) {
    boolean anyValueUpdated = false;
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public void updateLastReadAt() {
    lastReadAt = Instant.now();
  }
}
