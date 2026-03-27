package com.sprint.mission.discodeit.storage.exception;

import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public class StorageException extends BinaryContentException {

  public StorageException() {
    super(ErrorCode.STORAGE_ERROR, null);
  }
}
