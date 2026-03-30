package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class ReadStatusException extends DiscodeitException {

  public ReadStatusException(ErrorCode errorCode) {
    super(errorCode);
  }
}
