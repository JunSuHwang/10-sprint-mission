package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public class UserDuplicationException extends UserException {

  public UserDuplicationException(String userName) {
    super(ErrorCode.USER_DUPLICATION_ERROR);
    setDetails(Map.of("userName", userName));
  }
}
