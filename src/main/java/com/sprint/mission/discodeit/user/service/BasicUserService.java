package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.EmailDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentRepository contentRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentStorage storage;
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  @Override
  public UserDto createUser(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> image) {

    log.debug("[USER_CREATE] 사용자 등록 시작 username={}", request.username());
    validateUserExist(request.username());
    validateEmailExist(request.email());

    User user = userMapper.toEntity(request);

    UserStatus status = new UserStatus();
    status.setUser(user);

    if (image.isPresent()) {
      log.debug("[BINARY_CONTENT_UPLOAD] 프로필 업로드 시작 username={}, fileName={}", request.username(),
          image.get().fileName());
      BinaryContent profileImage = binaryContentMapper.toEntity(image.get());
      user.setProfile(profileImage);
      contentRepository.save(profileImage);
      storage.put(profileImage.getId(), image.get().bytes());
      log.debug("[BINARY_CONTENT_UPLOAD] 프로필 업로드 id={}", profileImage.getId());
    }
    userRepository.save(user);

    log.info("[USER_CREATE] 사용자 등록 id={}", user.getId());

    return userMapper.toDto(user);
  }

  @Override
  public UserDto findUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  public List<UserDto> findAllWithUserDTO() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public List<UserDto> findAllByChannelId(UUID channelId) {
    return userRepository.findAll()
        .stream()
        .filter(user -> readStatusRepository.existsByUserIdAndChannelId(user.getId(), channelId))
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto updateUser(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> image) {
    log.debug("[USER_UPDATE] 사용자 수정 시작 id={}", userId);

    Optional.ofNullable(request.newUsername())
        .ifPresent(this::validateUserExist);
    Optional.ofNullable(request.newEmail())
        .ifPresent(this::validateEmailExist);

    User findUser = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    userMapper.update(request, findUser);

    if (image.isPresent()) {
      log.debug("[BINARY_CONTENT_UPLOAD] 프로필 수정 시작 userId={}, fileName={}", findUser.getId(),
          image.get().fileName());
      if (findUser.isProfileImageUploaded()) {
        contentRepository.deleteById(findUser.getProfile().getId());
      }
      BinaryContent profileImage = binaryContentMapper.toEntity(image.get());
      findUser.setProfile(profileImage);
      contentRepository.save(profileImage);
      storage.put(profileImage.getId(), image.get().bytes());
      log.info("[BINARY_CONTENT_UPLOAD] 프로필 수정 id={}", profileImage.getId());
    }

    userStatusRepository.findByUserId(findUser.getId())
        .ifPresent(UserStatus::update);

    log.info("[USER_UPDATE] 사용자 수정 id={}", userId);

    return userMapper.toDto(findUser);
  }

  @Transactional
  @Override
  public void deleteUser(UUID userId) {
    log.debug("[USER_DELETE] 사용자 삭제 시작 id={}", userId);

    userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    userRepository.deleteById(userId);

    log.info("[USER_DELETE] 사용자 삭제 id={}", userId);
  }

  private void validateUserExist(String username) {
    if (userRepository.existsUserByUsername(username)) {
      throw new UserDuplicationException();
    }
  }

  private void validateEmailExist(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new EmailDuplicationException();
    }
  }
}
