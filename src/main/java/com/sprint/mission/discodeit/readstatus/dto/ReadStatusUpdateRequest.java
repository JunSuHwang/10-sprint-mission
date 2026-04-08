package com.sprint.mission.discodeit.readstatus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

@Schema(description = "수정할 읽음 상태 정보")
public record ReadStatusUpdateRequest(
    @PastOrPresent(message = "읽은 날짜는 현재보다 이전이어야 합니다.")
    Instant newLastReadAt
) {

}
