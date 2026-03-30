package com.sprint.mission.discodeit.common.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

  public static ErrorResponse of(DiscodeitException e) {
    return new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().name(),
        e.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getStatus().value()
    );
  }

  public static ErrorResponse of(MethodArgumentTypeMismatchException e,
      Map<String, Object> details) {
    return new ErrorResponse(
        Instant.now(),
        ErrorCode.INVALID_PARAMETER_TYPE.name(),
        ErrorCode.INVALID_PARAMETER_TYPE.getMessage(),
        details,
        e.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
  }

  public static ErrorResponse of(Exception e) {
    return new ErrorResponse(
        Instant.now(),
        ErrorCode.INTERNAL_SERVER_ERROR.name(),
        ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
        null,
        e.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
  }
}
