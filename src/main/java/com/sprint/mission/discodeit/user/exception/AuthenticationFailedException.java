package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public class AuthenticationFailedException extends UserException {

  public AuthenticationFailedException(String userName) {
    super(ErrorCode.AUTHENTICATION_FAILED);
    setDetails(Map.of("userName", userName));
  }
}
