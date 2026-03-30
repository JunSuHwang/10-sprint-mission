package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class BinaryContentException extends DiscodeitException {

  protected BinaryContentException(ErrorCode errorCode) {
    super(errorCode);
  }
}
