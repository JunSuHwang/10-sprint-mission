package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusDuplicationException extends ReadStatusException {

  public ReadStatusDuplicationException(UUID userId, UUID channelId) {
    super(ErrorCode.READ_STATUS_DUPLICATION_ERROR);
    setDetails(Map.of("userId", userId, "channelId", channelId));
  }
}
