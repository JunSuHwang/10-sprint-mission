package com.sprint.mission.discodeit.userstatus.entity;

import com.sprint.mission.discodeit.common.CommonEntity;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends CommonEntity {

  private static final long serialVersionUID = 1L;
  private final UUID userId;
  private final int loginLimitSeconds = 60 * 5;
  private Instant lastActiveAt;

  public UserStatus(UUID userId) {
    this.userId = userId;
    lastActiveAt = Instant.now();
  }

  public void update() {
    lastActiveAt = Instant.now();
  }

  public boolean isOnline() {
    return lastActiveAt.isAfter(Instant.now().minusSeconds(loginLimitSeconds));
  }
}
