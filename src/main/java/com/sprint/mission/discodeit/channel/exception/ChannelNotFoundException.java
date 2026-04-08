package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(UUID id) {
    super(ErrorCode.CHANNEL_NOT_FOUND);
    setDetails(Map.of("channelId", id));
  }
}
