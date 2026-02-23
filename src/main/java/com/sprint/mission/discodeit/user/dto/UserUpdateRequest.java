package com.sprint.mission.discodeit.user.dto;

public record UserUpdateRequest(
    String newUsername,
    String newPassword,
    String newEmail
) {

}
