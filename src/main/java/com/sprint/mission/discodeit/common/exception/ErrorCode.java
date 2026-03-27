package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),
  DUPLICATE_USER("해당 사용자가 이미 존재합니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
