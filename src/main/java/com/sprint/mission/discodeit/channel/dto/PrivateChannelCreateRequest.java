package com.sprint.mission.discodeit.channel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Schema(description = "Private Channel 생성 정보")
public record PrivateChannelCreateRequest(
    @Size(min = 2, message = "참가 유저는 최소 2명이어야 합니다.")
    List<UUID> participantIds
) {

}
