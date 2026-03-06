package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.LoginRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserDto login(LoginRequest loginRequest) {
    User findUser = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(UserNotFoundException::new);
    if (!findUser.getPassword().equals(loginRequest.password())) {
      throw new AuthenticationFailedException();
    }
    return userMapper.toDto(findUser);
  }
}
