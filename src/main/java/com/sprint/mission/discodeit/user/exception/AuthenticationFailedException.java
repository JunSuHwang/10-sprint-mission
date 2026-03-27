package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class AuthenticationFailedException extends UserException {

  public AuthenticationFailedException() {
    super(ErrorCode.AUTHENTICATION_FAILED, null);
  }
}
