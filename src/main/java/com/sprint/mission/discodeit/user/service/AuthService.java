package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.user.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public void updateRole(UserRoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(request.userId()));
    user.setRole(request.newRole());
    log.info("[UPDATE_ROLE] userId={} role={}", user.getId(), user.getRole());
  }
}
