package com.sprint.mission.discodeit.message.mapper;

import com.sprint.mission.discodeit.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import java.util.List;
import java.util.UUID;

public final class MessageMapper {

  private MessageMapper() {
  }

  public static MessageDto toMessageInfo(Message message) {
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getAuthorId(),
        message.getChannelId(),
        message.getAttachmentIds()
    );
  }

  public static MessageCreateRequest toMessageCreateInfo(
      String content,
      UUID authorId,
      UUID channelId,
      List<byte[]> attachments
  ) {
    return new MessageCreateRequest(
        content,
        authorId,
        channelId
    );
  }

  public static MessageUpdateRequest toMessageUpdateInfo(
      String content
  ) {
    return new MessageUpdateRequest(
        content
    );
  }
}
