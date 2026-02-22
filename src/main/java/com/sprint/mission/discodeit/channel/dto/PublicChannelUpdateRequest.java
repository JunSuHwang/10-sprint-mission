package com.sprint.mission.discodeit.channel.dto;

public record PublicChannelUpdateRequest(
    String newName,
    String newDescription
) {

}
