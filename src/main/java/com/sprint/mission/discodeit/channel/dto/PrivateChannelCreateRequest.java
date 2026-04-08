package com.sprint.mission.discodeit.channel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Schema(description = "Private Channel 생성 정보")
public record PrivateChannelCreateRequest(
    @NotEmpty(message = "참가할 id 목록은 비어 있을 수 없습니다.")
    @Size(min = 2, message = "참가 유저는 최소 2명이어야 합니다.")
    List<@NotNull(message = "참가 id는 null일 수 없습니다.") UUID> participantIds
) {

}
