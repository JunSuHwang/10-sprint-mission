package com.sprint.mission.discodeit.message.entity;

import com.sprint.mission.discodeit.common.CommonEntity;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends CommonEntity {

  private static final long serialVersionUID = 1L;
  private final UUID authorId;
  private final UUID channelId;
  private final List<UUID> attachmentIds;
  private String content;

  public Message(String content, UUID authorId, UUID channel, List<UUID> attachmentIds) {
    this.content = content;
    this.authorId = authorId;
    this.channelId = channel;
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    this.content = newContent;
    this.updateAt = Instant.now();
  }

  public List<UUID> getAttachmentIds() {
    return List.copyOf(attachmentIds);
  }

  public void addAttachmentId(UUID attachmentId) {
    attachmentIds.add(attachmentId);
  }

  public void removeAttachmentId(UUID attachmentId) {
    attachmentIds.remove(attachmentId);
  }
}
