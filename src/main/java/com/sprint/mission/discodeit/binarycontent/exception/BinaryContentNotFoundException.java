package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException() {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND, null);
  }
}
