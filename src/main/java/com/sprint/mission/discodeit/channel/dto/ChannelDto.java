package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    String name,
    ChannelType type,
    String description,
    Instant lastMessageAt,
    List<UUID> participantIds
) {

}
