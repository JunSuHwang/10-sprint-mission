package com.sprint.mission.discodeit.userstatus.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException() {
    super(ErrorCode.USER_STATUS_NOT_FOUND);
  }

  public static UserStatusNotFoundException ById(UUID id) {
    UserStatusNotFoundException exception = new UserStatusNotFoundException();
    exception.setDetails(Map.of("userStatusId", id));
    return exception;
  }

  public static UserStatusNotFoundException ByUserId(UUID userId) {
    UserStatusNotFoundException exception = new UserStatusNotFoundException();
    exception.setDetails(Map.of("userId", userId));
    return exception;
  }
}
