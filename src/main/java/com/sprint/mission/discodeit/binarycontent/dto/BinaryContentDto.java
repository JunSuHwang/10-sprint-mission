package com.sprint.mission.discodeit.binarycontent.dto;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    Instant createdAt,
    String fileName,
    String contentType,
    byte[] bytes
) {

}
