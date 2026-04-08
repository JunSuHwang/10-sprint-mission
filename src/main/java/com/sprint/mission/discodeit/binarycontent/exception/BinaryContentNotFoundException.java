package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(UUID id) {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND);
    setDetails(Map.of("contentId", id));
  }
}
