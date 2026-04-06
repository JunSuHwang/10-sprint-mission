package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ChannelUpdateNotAllowedException extends ChannelException {

  public ChannelUpdateNotAllowedException(UUID id) {
    super(ErrorCode.CHANNEL_UPDATE_NOT_ALLOWED);
    setDetails(Map.of("channelId", id));
  }
}
