package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND, null);
  }
}
