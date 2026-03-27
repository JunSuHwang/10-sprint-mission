package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusDuplicationException;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.userstatus.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  public UserStatusDto createUserStatus(UserStatusCreateRequest request) {
    User findUser = userRepository.findById(request.userId())
        .orElseThrow(UserNotFoundException::new);
    if (userStatusRepository.existsByUserId(request.userId())) {
      throw new UserStatusDuplicationException();
    }

    UserStatus userStatus = new UserStatus();
    userStatus.setUser(findUser);

    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  public UserStatusDto findUserStatus(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(UserStatusNotFoundException::new);
    return userStatusMapper.toDto(userStatus);
  }

  public UserStatusDto findUserStatusByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(UserStatusNotFoundException::new);
    return userStatusMapper.toDto(userStatus);
  }

  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll()
        .stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  public UserStatusDto update(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(UserStatusNotFoundException::new);
    userStatus.update();
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  public UserStatusDto updateUserStatusByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(UserStatusNotFoundException::new);
    userStatus.update();
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  public UserStatusDto update(UUID userId, UserStatusUpdateRequest request) {

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(UserStatusNotFoundException::new);
    userStatus.update(request.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  void deleteUserStatus(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }
}