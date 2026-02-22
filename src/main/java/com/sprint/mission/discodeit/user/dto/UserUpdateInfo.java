package com.sprint.mission.discodeit.user.dto;

public record UserUpdateInfo(
    String newUsername,
    String newPassword,
    String newEmail
) {

}
