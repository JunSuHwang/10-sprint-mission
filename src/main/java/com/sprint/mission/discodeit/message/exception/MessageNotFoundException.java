package com.sprint.mission.discodeit.message.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException() {
    super(ErrorCode.MESSAGE_NOT_FOUND, null);
  }
}
