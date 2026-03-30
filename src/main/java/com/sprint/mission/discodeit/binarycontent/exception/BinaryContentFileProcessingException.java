package com.sprint.mission.discodeit.binarycontent.exception;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public class BinaryContentFileProcessingException extends BinaryContentException {

  public BinaryContentFileProcessingException(MultipartFile file) {
    super(ErrorCode.FILE_PROCESSING_ERROR);

    String fileName = file.getOriginalFilename();
    if (fileName == null) {
      fileName = "unknown";
    }

    setDetails(Map.of("fileName", fileName, "fileSize", file.getSize()));
  }
}
