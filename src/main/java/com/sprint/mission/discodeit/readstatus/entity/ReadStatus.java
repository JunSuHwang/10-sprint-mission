package com.sprint.mission.discodeit.readstatus.entity;

import com.sprint.mission.discodeit.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @Column(nullable = false)
  private Instant lastReadAt;

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
