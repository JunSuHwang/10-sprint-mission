package com.sprint.mission.discodeit.user.dto;

import java.util.UUID;

public record UserDtoWithStatus(
    UUID id,
    String username,
    String email,
    UUID profileId,
    UUID statusId,
    boolean online
) {

}
