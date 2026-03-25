package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.exception.BusinessException;

public class BinaryContentFileProcessingException extends BusinessException {

  public BinaryContentFileProcessingException() {
    super("첨부 파일 처리 중 오류가 발생했습니다.");
  }
}
