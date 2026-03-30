package com.sprint.mission.discodeit.userstatus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

@Schema(description = "변경할 User 온라인 상태 정보")
public record UserStatusUpdateRequest(
    @PastOrPresent(message = "마지막 접속 날짜는 현재보다 이전이어야 합니다.")
    Instant newLastActiveAt
) {

}
