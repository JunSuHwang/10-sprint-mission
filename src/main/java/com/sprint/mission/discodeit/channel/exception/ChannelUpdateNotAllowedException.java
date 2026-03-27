package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class ChannelUpdateNotAllowedException extends ChannelException {

  public ChannelUpdateNotAllowedException() {
    super(ErrorCode.CHANNEL_UPDATE_NOT_ALLOWED, null);
  }
}
