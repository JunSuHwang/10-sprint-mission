package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(UUID id) {
    super(ErrorCode.USER_NOT_FOUND);
    setDetails(Map.of("userId", id));
  }

  public UserNotFoundException(String username) {
    super(ErrorCode.USER_NOT_FOUND);
    setDetails(Map.of("username", username));
  }

  public static UserNotFoundException ById(UUID id) {
    UserNotFoundException exception = new UserNotFoundException(id);
    exception.setDetails(Map.of("userId", id));
    return exception;
  }

  public static UserNotFoundException ByUserName(String username) {
    UserNotFoundException exception = new UserNotFoundException(username);
    exception.setDetails(Map.of("username", username));
    return exception;
  }
}
