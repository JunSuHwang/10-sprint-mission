package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class ChannelException extends DiscodeitException {


  public ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }
}
