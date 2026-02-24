package com.sprint.mission.discodeit.userstatus.mapper;

import com.sprint.mission.discodeit.userstatus.dto.UserStatusDto;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;

public class UserStatusMapper {

  private UserStatusMapper() {
  }

  public static UserStatusDto toUserStatusInfo(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdatedAt(),
        userStatus.getUserId(),
        userStatus.getLastActiveAt(),
        userStatus.isOnline()
    );
  }


}
