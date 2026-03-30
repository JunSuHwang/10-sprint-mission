package com.sprint.mission.discodeit.binarycontent.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@NotEmpty(message = "조회할 id 목록은 비어 있을 수 없습니다.")
@Size(max = 100, message = "최대 100개까지 조회할 수 있습니다.")
public record BinaryContentsRequest(
    List<@NotNull(message = "조회 id는 null일 수 없습니다.") UUID> ids
) {

}
