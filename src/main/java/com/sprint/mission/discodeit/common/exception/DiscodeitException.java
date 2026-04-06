package com.sprint.mission.discodeit.common.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  @Setter
  private Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
  }

}
