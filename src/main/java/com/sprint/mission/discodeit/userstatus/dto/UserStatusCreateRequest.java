package com.sprint.mission.discodeit.userstatus.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull
    UUID userId,

    @NotNull
    @PastOrPresent(message = "마지막 접속 날짜는 현재보다 이전이어야 합니다.")
    Instant lastActiveAt
) {

}
