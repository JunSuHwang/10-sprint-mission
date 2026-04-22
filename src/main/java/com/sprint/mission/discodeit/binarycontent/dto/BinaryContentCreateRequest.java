package com.sprint.mission.discodeit.binarycontent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일 이름은 필수입니다.")
    String fileName,

    @NotBlank(message = "Content Type은 필수입니다.")
    String contentType,

    @NotNull(message = "파일 데이터는 필수입니다.")
    byte[] bytes
) {

}
