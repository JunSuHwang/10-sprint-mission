package com.sprint.mission.discodeit.storage.exception;

import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class StorageException extends BinaryContentException {

  public StorageException(UUID id) {
    super(ErrorCode.STORAGE_ERROR);
    if (id != null) {
      setDetails(Map.of("contentId", id));
    }
  }
}
