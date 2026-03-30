package com.sprint.mission.discodeit.message.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(UUID id) {
    super(ErrorCode.MESSAGE_NOT_FOUND);
    setDetails(Map.of("messageId", id));
  }
}
