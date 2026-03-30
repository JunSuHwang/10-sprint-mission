package com.sprint.mission.discodeit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User 생성 정보")
public record UserCreateRequest(
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Size(min = 3, max = 50, message = "이름은 최소 3자 이상 50자 이하입니다.")
    String username,

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Size(min = 6, max = 100, message = "비밀번호는 최소 6자 이상 100자 이하입니다.")
    String password,

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email
) {

}
