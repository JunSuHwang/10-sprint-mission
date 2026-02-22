package com.sprint.mission.discodeit.userstatus.dto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    UUID statusId,
    UUID userId,
    Instant lastOnlineAt,
    boolean isOnline
) {

}
