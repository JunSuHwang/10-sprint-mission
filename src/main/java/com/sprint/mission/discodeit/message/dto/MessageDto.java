package com.sprint.mission.discodeit.message.dto;

import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID id,
    String content,
    UUID authorId,
    UUID channelId,
    List<UUID> attachmentIds
) {

}
