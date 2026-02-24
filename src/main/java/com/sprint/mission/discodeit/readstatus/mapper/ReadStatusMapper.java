package com.sprint.mission.discodeit.readstatus.mapper;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;

public class ReadStatusMapper {

  private ReadStatusMapper() {
  }

  public static ReadStatusDto toReadStatusInfo(ReadStatus readStatus) {
    return new ReadStatusDto(
        readStatus.getId(),
        readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(),
        readStatus.getUserId(),
        readStatus.getChannelId(),
        readStatus.getLastReadAt()
    );
  }

  public static ReadStatusCreateRequest toReadStatusCreateInfo(ReadStatus readStatus) {
    return new ReadStatusCreateRequest(
        readStatus.getUserId(),
        readStatus.getChannelId(),
        readStatus.getLastReadAt()
    );
  }

  public static ReadStatus toReadStatus(ReadStatusCreateRequest readStatusCreateRequest) {
    return new ReadStatus(
        readStatusCreateRequest.userId(),
        readStatusCreateRequest.channelId()
    );
  }
}
