package com.sprint.mission.discodeit.userstatus.dto;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
