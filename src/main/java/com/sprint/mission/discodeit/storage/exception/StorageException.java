package com.sprint.mission.discodeit.storage.exception;

import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class StorageException extends BinaryContentException {

  public StorageException(UUID id) {
    super(ErrorCode.STORAGE_ERROR);
    if (id != null) {
      setDetails(Map.of("contentId", id));
    }
  }

  public StorageException(UUID id, Throwable cause) {
    super(ErrorCode.STORAGE_ERROR);

    Map<String, Object> details = new HashMap<>();

    if (id != null) {
      details.put("contentId", id);
    }

    if (cause instanceof S3Exception s3) {
      details.putAll(extractS3Details(s3));
    }

    if (!details.isEmpty()) {
      setDetails(details);
    }
  }

  private Map<String, Object> extractS3Details(S3Exception e) {
    Map<String, Object> map = new HashMap<>();

    map.put("s3Message", e.awsErrorDetails() != null
        ? e.awsErrorDetails().errorMessage()
        : e.getMessage());

    map.put("errorCode", e.awsErrorDetails() != null
        ? e.awsErrorDetails().errorCode()
        : null);

    map.put("statusCode", e.statusCode());
    map.put("requestId", e.requestId());

    return map;
  }
}
