package com.sprint.mission.discodeit.readstatus.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID statusId,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
