package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;

public record ChannelResultDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    ChannelType type,
    String name,
    String description
) {

}
