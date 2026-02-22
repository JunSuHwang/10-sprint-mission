package com.sprint.mission.discodeit.user.dto;

public record UserCreateRequest(
    String username,
    String password,
    String email
) {

}
