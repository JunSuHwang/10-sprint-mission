package com.sprint.mission.discodeit.user.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResultDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId
) {

}
