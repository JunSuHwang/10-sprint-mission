package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class UserException extends DiscodeitException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }
}
