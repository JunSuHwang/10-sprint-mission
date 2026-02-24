package com.sprint.mission.discodeit.binarycontent.mapper;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;

public class BinaryContentMapper {

  private BinaryContentMapper() {
  }

  public static BinaryContentDto toBinaryContentDto(BinaryContent binaryContent) {
    return new BinaryContentDto(
        binaryContent.getId(),
        binaryContent.getCreatedAt(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType(),
        binaryContent.getBytes()
    );
  }
}
