package com.sprint.mission.discodeit.readstatus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Message 읽음 상태 생성 정보")
public record ReadStatusCreateRequest(
    @NotNull
    UUID userId,

    @NotNull
    UUID channelId,

    @NotNull
    @PastOrPresent(message = "읽은 날짜는 현재보다 이전이어야 합니다.")
    Instant lastReadAt
) {

}
