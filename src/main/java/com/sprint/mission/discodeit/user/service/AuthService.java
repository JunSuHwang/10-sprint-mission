package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.LoginRequest;
import com.sprint.mission.discodeit.user.dto.UserInfo;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  public UserInfo login(LoginRequest loginRequest) {
    User findUser = userRepository.findByName(loginRequest.username())
        .orElseThrow(UserNotFoundException::new);
    if (!findUser.getPassword().equals(loginRequest.password())) {
      throw new AuthenticationFailedException();
    }
    UserStatus status = userStatusRepository.findByUserId(findUser.getId())
        .orElseThrow(UserStatusNotFoundException::new);
    ;
    status.update();
    userStatusRepository.save(status);
    return UserMapper.toUserInfo(findUser, status);
  }
}
