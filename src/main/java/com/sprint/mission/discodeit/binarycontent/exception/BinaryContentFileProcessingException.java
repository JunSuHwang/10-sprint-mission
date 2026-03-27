package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class BinaryContentFileProcessingException extends BinaryContentException {

  public BinaryContentFileProcessingException() {
    super(ErrorCode.FILE_PROCESSING_ERROR, null);
  }
}
