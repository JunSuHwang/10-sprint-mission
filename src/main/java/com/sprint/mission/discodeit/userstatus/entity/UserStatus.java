package com.sprint.mission.discodeit.userstatus.entity;

import com.sprint.mission.discodeit.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;

  private final int loginLimitSeconds = 60 * 5;

  public UserStatus() {
    lastActiveAt = Instant.now();
  }

  public void update() {
    lastActiveAt = Instant.now();
  }

  public void update(Instant newLastActiveAt) {
    lastActiveAt = newLastActiveAt;
  }

  public void setUser(User user) {
    this.user = user;
    user.setUserStatus(this);
  }

  public boolean isOnline() {
    return lastActiveAt.isAfter(Instant.now().minusSeconds(loginLimitSeconds));
  }
}
