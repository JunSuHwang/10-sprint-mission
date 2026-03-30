package com.sprint.mission.discodeit.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "Message 생성 정보")
public record MessageCreateRequest(
    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    @Size(max = 500, message = "메시지 내용은 500자 이하여야 합니다.")
    String content,

    @NotNull
    UUID authorId,

    @NotNull
    UUID channelId
) {

}
