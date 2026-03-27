package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {

  protected BinaryContentException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
