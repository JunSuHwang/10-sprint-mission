package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class EmailDuplicationException extends UserException {

  public EmailDuplicationException() {
    super(ErrorCode.EMAIL_DUPLICATION_ERROR, null);
  }
}
