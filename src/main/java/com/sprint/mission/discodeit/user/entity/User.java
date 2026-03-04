package com.sprint.mission.discodeit.user.entity;

import com.sprint.mission.discodeit.base.BaseUpdatableEntity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
public class User extends BaseUpdatableEntity {

  private final List<UUID> channelIds = new ArrayList<>();
  private final List<UUID> messageIds = new ArrayList<>();
  private String username;
  private String password;
  private String email;
  @Setter
  private UUID profileId;

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public List<UUID> getChannelIds() {
    return List.copyOf(channelIds);
  }

  public List<UUID> getMessageIds() {
    return List.copyOf(messageIds);
  }

  public void updateUserName(String username) {
    this.username = username;
    this.updatedAt = Instant.now();
  }

  public void updatePassword(String password) {
    this.password = password;
    this.updatedAt = Instant.now();
  }

  public void updateEmail(String email) {
    this.email = email;
    this.updatedAt = Instant.now();
  }

  public void addChannelId(UUID channelId) {
    channelIds.add(channelId);
    this.updatedAt = Instant.now();
  }

  public void removeChannelId(UUID channelId) {
    channelIds.remove(channelId);
    this.updatedAt = Instant.now();
  }

  public void addMessageId(UUID messageId) {
    messageIds.add(messageId);
    this.updatedAt = Instant.now();
  }

  public void removeMessageId(UUID messageId) {
    messageIds.remove(messageId);
    this.updatedAt = Instant.now();
  }

  public boolean isProfileImageUploaded() {
    return profileId != null;
  }
}
