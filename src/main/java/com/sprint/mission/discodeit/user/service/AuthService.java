package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.LoginRequest;
import com.sprint.mission.discodeit.user.dto.UserResultDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserRepository userRepository;

  public UserResultDto login(LoginRequest loginRequest) {
    User findUser = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(UserNotFoundException::new);
    if (!findUser.getPassword().equals(loginRequest.password())) {
      throw new AuthenticationFailedException();
    }
    return UserMapper.toUserResultDto(findUser);
  }
}
