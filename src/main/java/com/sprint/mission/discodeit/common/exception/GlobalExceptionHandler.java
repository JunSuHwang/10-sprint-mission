package com.sprint.mission.discodeit.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    ErrorResponse response = ErrorResponse.of(e);
    log.warn("[{}] {}", response.code(), response.message());
    return ResponseEntity.status(response.status()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handle(Exception e) {
    ErrorResponse response = ErrorResponse.of(e);
    log.error("[{}] {}", response.code(), response.message());
    return ResponseEntity.status(response.status()).body(response);
  }
}
