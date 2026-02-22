package com.sprint.mission.discodeit.channel.entity;

import com.sprint.mission.discodeit.common.ChannelType;
import com.sprint.mission.discodeit.common.CommonEntity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel extends CommonEntity {

  private static final long serialVersionUID = 1L;
  private final List<UUID> messageIds = new ArrayList<>();
  private final List<UUID> userIds = new ArrayList<>();
  private String name;
  private ChannelType type;
  private String description;

  public Channel(String name, ChannelType type, String description) {
    this.name = name;
    this.type = type;
    this.description = description;
  }

  public List<UUID> getMessageIds() {
    return List.copyOf(messageIds);
  }

  public List<UUID> getUserIds() {
    return List.copyOf(userIds);
  }

  public void updateChannelName(String channelName) {
    this.name = channelName;
    this.updateAt = Instant.now();
  }

  public void updateChannelType(ChannelType channelType) {
    this.type = channelType;
    this.updateAt = Instant.now();
  }

  public void updateDescription(String description) {
    this.description = description;
    this.updateAt = Instant.now();
  }

  public void addMessageId(UUID messageId) {
    messageIds.add(messageId);
    this.updateAt = Instant.now();
  }

  public void removeMessageId(UUID messageId) {
    messageIds.remove(messageId);
    this.updateAt = Instant.now();
  }

  public void addUserId(UUID userId) {
    userIds.add(userId);
    this.updateAt = Instant.now();
  }

  public void removeUserId(UUID userId) {
    userIds.remove(userId);
    this.updateAt = Instant.now();
  }
}
