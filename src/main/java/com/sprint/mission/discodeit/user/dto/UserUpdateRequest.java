package com.sprint.mission.discodeit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 User 정보")
public record UserUpdateRequest(
    @Size(min = 3, max = 50, message = "이름은 최소 3자 이상 50자 이하입니다.")
    String newUsername,

    @Size(min = 6, max = 100, message = "비밀번호는 최소 6자 이상 100자 이하입니다.")
    String newPassword,

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String newEmail
) {

}
