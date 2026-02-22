package com.sprint.mission.discodeit.message.dto;

import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID authorId,
    UUID channelId
) {

}
