package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserInfo;
import com.sprint.mission.discodeit.user.dto.UserDtoWithStatus;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;

public class UserMapper {

  private UserMapper() {
  }

  public static UserInfo toUserInfo(User user, UserStatus userStatus) {
    return new UserInfo(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        userStatus.getId()
    );
  }

  public static UserDtoWithStatus toUserInfoWithStatus(User user, UserStatus userStatus) {
    return new UserDtoWithStatus(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        userStatus.getId(),
        userStatus.isOnline()
    );
  }

  public static UserDto toUserDto(User user, UserStatus userStatus) {
    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        userStatus.isOnline()
    );
  }

  public static User toUser(UserCreateRequest userInfo) {
    return new User(
        userInfo.username(),
        userInfo.password(),
        userInfo.email()
    );
  }
}
