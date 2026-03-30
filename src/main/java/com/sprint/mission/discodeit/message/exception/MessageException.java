package com.sprint.mission.discodeit.message.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode) {
    super(errorCode);
  }
}
