package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class ReadStatusDuplicationException extends ReadStatusException {

  public ReadStatusDuplicationException() {
    super(ErrorCode.READ_STATUS_DUPLICATION_ERROR, null);
  }
}
