package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class AlreadyJoinedException extends ChannelException {

  public AlreadyJoinedException(UUID channelId, UUID userId) {
    super(ErrorCode.ALREADY_JOINED);
    setDetails(Map.of("channelId", channelId, "userId", userId));
  }
}
