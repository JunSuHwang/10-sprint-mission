package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserDto createUser(UserCreateRequest userInfo, Optional<BinaryContentCreateRequest> image);

  UserDto findUser(UUID userId);

  List<UserDto> findAll();

  List<UserDto> findAllWithUserDTO();

  List<UserDto> findAllByChannelId(UUID channelId);

  UserDto updateUser(UUID userId, UserUpdateRequest updateInfo,
      Optional<BinaryContentCreateRequest> image);

  void deleteUser(UUID userId);
}
