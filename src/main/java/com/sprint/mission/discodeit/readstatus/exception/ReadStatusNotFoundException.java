package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException() {
    super(ErrorCode.READ_STATUS_NOT_FOUND, null);
  }
}
