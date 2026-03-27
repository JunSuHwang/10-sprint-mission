package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class ChannelDuplicationException extends ChannelException {

  public ChannelDuplicationException() {
    super(ErrorCode.CHANNEL_DUPLICATION_ERROR, null);
  }
}
