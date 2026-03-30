package com.sprint.mission.discodeit.userstatus.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusDuplicationException extends UserStatusException {

  public UserStatusDuplicationException(UUID userId) {
    super(ErrorCode.USER_STATUS_DUPLICATION_ERROR);
    setDetails(Map.of("userId", userId));
  }
}
