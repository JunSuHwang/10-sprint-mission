package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class AlreadyJoinedException extends ChannelException {

  public AlreadyJoinedException() {
    super(ErrorCode.ALREADY_JOINED, null);
  }
}
