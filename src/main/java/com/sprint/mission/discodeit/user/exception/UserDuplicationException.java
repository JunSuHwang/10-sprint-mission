package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class UserDuplicationException extends UserException {

  public UserDuplicationException() {
    super(ErrorCode.USER_DUPLICATION_ERROR, null);
  }
}
