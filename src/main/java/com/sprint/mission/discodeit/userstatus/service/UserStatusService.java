package com.sprint.mission.discodeit.userstatus.service;

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
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  public UserStatusDto createUserStatus(UserStatusCreateRequest statusInfo) {
    userRepository.findById(statusInfo.userId())
        .orElseThrow(UserNotFoundException::new);
    if (userStatusRepository.findByUserId(statusInfo.userId())
        .isPresent()) {
      throw new UserStatusDuplicationException();
    }
    UserStatus userStatus = new UserStatus(statusInfo.userId());
    userStatusRepository.save(userStatus);
    return UserStatusMapper.toUserStatusInfo(userStatus);
  }

  public UserStatusDto findUserStatus(UUID statusId) {
    UserStatus userStatus = userStatusRepository.findById(statusId)
        .orElseThrow(UserStatusNotFoundException::new);
    return UserStatusMapper.toUserStatusInfo(userStatus);
  }

  public UserStatusDto findUserStatusByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(UserStatusNotFoundException::new);
    return UserStatusMapper.toUserStatusInfo(userStatus);
  }

  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll()
        .stream()
        .map(UserStatusMapper::toUserStatusInfo)
        .toList();
  }

  public UserStatusDto update(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(UserStatusNotFoundException::new);
    userStatus.update();
    userStatusRepository.save(userStatus);
    return UserStatusMapper.toUserStatusInfo(userStatus);
  }

  public UserStatusDto updateUserStatusByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(UserStatusNotFoundException::new);
    userStatus.update();
    userStatusRepository.save(userStatus);
    return UserStatusMapper.toUserStatusInfo(userStatus);
  }

  public UserStatusDto update(UUID userId, UserStatusUpdateRequest request) {

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(UserStatusNotFoundException::new);
    userStatus.update(request.newLastActiveAt());
    userStatusRepository.save(userStatus);
    return UserStatusMapper.toUserStatusInfo(userStatus);
  }

  void deleteUserStatus(UUID statusId) {
    userStatusRepository.deleteById(statusId);
  }
}
