package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
  USER_DUPLICATION_ERROR(HttpStatus.CONFLICT, "해당 사용자가 이미 존재합니다."),
  EMAIL_DUPLICATION_ERROR(HttpStatus.CONFLICT, "해당 이메일이 이미 존재합니다."),
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자 상태를 찾을 수 없습니다."),
  USER_STATUS_DUPLICATION_ERROR(HttpStatus.CONFLICT, "해당 사용자 상태가 이미 존재합니다."),
  AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채널을 찾을 수 없습니다."),
  CHANNEL_DUPLICATION_ERROR(HttpStatus.CONFLICT, "해당 채널이 이미 존재합니다."),
  CHANNEL_UPDATE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "해당 채널은 수정할 수 없습니다."),
  ALREADY_JOINED(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메시지를 찾을 수 없습니다."),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 수신 정보가 존재하지 않습니다."),
  READ_STATUS_DUPLICATION_ERROR(HttpStatus.CONFLICT, "해당 수신 정보가 이미 존재합니다."),
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 콘텐츠를 찾을 수 없습니다."),
  FILE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "첨부 파일 처리 중 오류가 발생했습니다."),
  STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 및 불러오는 중 오류가 발생했습니다."),
  INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "해당 파라미터가 유효하지 않습니다."),
  UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력 형식이 올바르지 않습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
