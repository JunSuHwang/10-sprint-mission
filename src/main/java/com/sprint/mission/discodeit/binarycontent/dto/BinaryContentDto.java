package com.sprint.mission.discodeit.binarycontent.dto;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
    UUID contentId,
    Instant createdAt,
    String fileName,
    String contentType,
    byte[] content
) {

}
