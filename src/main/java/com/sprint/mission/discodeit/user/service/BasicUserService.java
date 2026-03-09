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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    validateUserExist(request.username());
    validateEmailExist(request.email());

    User user = userMapper.toEntity(request);

    UserStatus status = new UserStatus();
    status.setUser(user);

    if (image.isPresent()) {
      BinaryContent profileImage = binaryContentMapper.toEntity(image.get());
      user.setProfile(profileImage);
      contentRepository.save(profileImage);
      storage.put(profileImage.getId(), image.get().bytes());
    }
    userRepository.save(user);
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
    Optional.ofNullable(request.newUsername())
        .ifPresent(this::validateUserExist);
    Optional.ofNullable(request.newEmail())
        .ifPresent(this::validateEmailExist);

    User findUser = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    userMapper.update(request, findUser);

    if (image.isPresent()) {
      if (findUser.isProfileImageUploaded()) {
        contentRepository.deleteById(findUser.getProfile().getId());
      }
      BinaryContent profileImage = binaryContentMapper.toEntity(image.get());
      findUser.setProfile(profileImage);
      contentRepository.save(profileImage);
      storage.put(profileImage.getId(), image.get().bytes());
    }

    userStatusRepository.findByUserId(findUser.getId())
        .ifPresent(UserStatus::update);

    return userMapper.toDto(findUser);
  }

  @Transactional
  @Override
  public void deleteUser(UUID userId) {
    userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    userRepository.deleteById(userId);
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
