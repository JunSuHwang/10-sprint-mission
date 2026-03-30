package com.sprint.mission.discodeit.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 Message 내용")
public record MessageUpdateRequest(
    @Size(max = 500, message = "메시지 내용은 500자 이하여야 합니다.")
    String newContent
) {

}
