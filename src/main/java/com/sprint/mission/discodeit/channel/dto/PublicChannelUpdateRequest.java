package com.sprint.mission.discodeit.channel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 Channel 정보")
public record PublicChannelUpdateRequest(
    String newName,

    @Size(max = 255)
    String newDescription
) {

}
