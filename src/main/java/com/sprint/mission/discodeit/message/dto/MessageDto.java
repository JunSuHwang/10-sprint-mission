package com.sprint.mission.discodeit.message.dto;

import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID messageId,
    String content,
    UUID senderId,
    UUID channelId,
    List<UUID> attachmentIds
) {

}
