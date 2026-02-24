package com.sprint.mission.discodeit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User 생성 정보")
public record UserCreateRequest(
    String username,
    String password,
    String email
) {

}
