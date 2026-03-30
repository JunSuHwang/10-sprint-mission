package com.sprint.mission.discodeit.userstatus.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class UserStatusException extends DiscodeitException {

  public UserStatusException(ErrorCode errorCode) {
    super(errorCode);
  }
}
