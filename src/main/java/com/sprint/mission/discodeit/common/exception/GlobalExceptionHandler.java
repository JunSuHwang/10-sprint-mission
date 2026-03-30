package com.sprint.mission.discodeit.common.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handle(DiscodeitException e) {
    ErrorResponse response = ErrorResponse.of(e);
    if (e.getErrorCode().getStatus().is5xxServerError()) {
      log.error("errorCode={}, message={}, details={}",
          response.code(),
          response.message(),
          response.details()
      );
    } else {
      log.warn("errorCode={}, message={}, details={}",
          response.code(),
          response.message(),
          response.details()
      );
    }
    return ResponseEntity.status(response.status()).body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handle(MethodArgumentTypeMismatchException e) {
    Map<String, Object> details = new HashMap<>();
    details.put("parameter", e.getName());
    details.put("value", e.getValue());
    details.put("requiredType",
        e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown"
    );

    ErrorResponse response = ErrorResponse.of(e, details);
    log.warn("[{}] {} details={}", response.code(), response.message(), response.details());
    return ResponseEntity.status(response.status()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
    ErrorResponse response = ErrorResponse.of(e);
    log.error("[{}] {} details={}", response.code(), response.message(), response.details());
    return ResponseEntity.status(response.status()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handle(Exception e) {
    ErrorResponse response = ErrorResponse.of(e);
    log.error("[{}] {}", response.code(), response.message());
    return ResponseEntity.status(response.status()).body(response);
  }
}
