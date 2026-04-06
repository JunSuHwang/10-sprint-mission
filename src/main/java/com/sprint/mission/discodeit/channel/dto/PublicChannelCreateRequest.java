package com.sprint.mission.discodeit.channel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Public Channel 생성 정보")
public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름은 공백일 수 없습니다.")
    String name,

    @Size(max = 255)
    String description
) {

}
