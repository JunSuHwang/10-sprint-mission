package com.sprint.mission.discodeit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 정보")
public record LoginRequest(
    @NotBlank(message = "사용자 이름을 입력하세요.")
    @Size(max = 50)
    String username,

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(max = 100)
    String password
) {

}
