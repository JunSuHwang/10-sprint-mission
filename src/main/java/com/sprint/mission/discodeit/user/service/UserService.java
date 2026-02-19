package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateInfo;
import com.sprint.mission.discodeit.user.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserInfo createUser(UserCreateInfo userInfo, Optional<BinaryContentCreateInfo> image);
    UserInfoWithStatus findUser(UUID userId);
    List<UserInfoWithStatus> findAll();
    List<UserDto> findAllWithUserDTO();
    List<UserInfoWithStatus> findAllByChannelId(UUID channelId);
    UserInfo updateUser(UUID userId, UserUpdateInfo updateInfo, Optional<BinaryContentCreateInfo> image);
    void deleteUser(UUID userId);
}
