package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserInfo;
import com.sprint.mission.discodeit.user.dto.UserDtoWithStatus;
import com.sprint.mission.discodeit.user.dto.UserUpdateInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserInfo createUser(UserCreateRequest userInfo, Optional<BinaryContentCreateRequest> image);

  UserDtoWithStatus findUser(UUID userId);

  List<UserDtoWithStatus> findAll();

  List<UserDto> findAllWithUserDTO();

  List<UserDtoWithStatus> findAllByChannelId(UUID channelId);

  UserInfo updateUser(UUID userId, UserUpdateInfo updateInfo,
      Optional<BinaryContentCreateRequest> image);

  void deleteUser(UUID userId);
}
