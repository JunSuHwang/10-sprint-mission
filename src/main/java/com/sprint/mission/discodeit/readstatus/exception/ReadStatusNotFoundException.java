package com.sprint.mission.discodeit.readstatus.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(UUID id) {
    super(ErrorCode.READ_STATUS_NOT_FOUND);
    setDetails(Map.of("readStatusId", id));
  }
}
