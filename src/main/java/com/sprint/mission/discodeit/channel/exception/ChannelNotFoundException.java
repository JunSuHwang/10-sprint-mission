package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException() {
    super(ErrorCode.CHANNEL_NOT_FOUND, null);
  }
}
