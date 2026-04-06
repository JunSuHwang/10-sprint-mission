package com.sprint.mission.discodeit.channel.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;

public class ChannelDuplicationException extends ChannelException {

  public ChannelDuplicationException(String name) {
    super(ErrorCode.CHANNEL_DUPLICATION_ERROR);
    setDetails(Map.of("channelName", name));
  }
}
