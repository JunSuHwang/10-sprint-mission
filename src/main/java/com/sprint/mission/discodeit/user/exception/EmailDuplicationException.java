package com.sprint.mission.discodeit.user.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public class EmailDuplicationException extends UserException {

  public EmailDuplicationException(String email) {
    super(ErrorCode.EMAIL_DUPLICATION_ERROR);
    setDetails(Map.of("email", email));
  }
}
