package com.sprint.mission.discodeit.channel.entity;

import com.sprint.mission.discodeit.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  public Channel(String name, ChannelType type, String description) {
    this.name = name;
    this.type = type;
    this.description = description;
  }

  public void updateChannelName(String channelName) {
    this.name = channelName;
    this.updatedAt = Instant.now();
  }

  public void updateChannelType(ChannelType channelType) {
    this.type = channelType;
    this.updatedAt = Instant.now();
  }

  public void updateDescription(String description) {
    this.description = description;
    this.updatedAt = Instant.now();
  }
}
