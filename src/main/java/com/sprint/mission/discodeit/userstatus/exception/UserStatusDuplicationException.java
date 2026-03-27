package com.sprint.mission.discodeit.userstatus.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class UserStatusDuplicationException extends UserStatusException {

  public UserStatusDuplicationException() {
    super(ErrorCode.USER_STATUS_DUPLICATION_ERROR, null);
  }
}
