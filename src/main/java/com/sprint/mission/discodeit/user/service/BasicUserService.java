package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserResultDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.EmailDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository contentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserResultDto createUser(UserCreateRequest userInfo,
      Optional<BinaryContentCreateRequest> image) {
    // 유저 이름 & 이메일 검증
    validateUserExist(userInfo.username());
    validateEmailExist(userInfo.email());

    // 유저 생성 -> mapper로 대체 가능
    User user = new User(userInfo.username(), userInfo.password(), userInfo.email());

    // status 생성
    UserStatus status = new UserStatus(user.getId());

    // profile image가 존재한다면 생성
    if (image.isPresent()) {
      BinaryContentCreateRequest createInfo = image.get();
      byte[] bytes = createInfo.bytes();
      BinaryContent profileImage = new BinaryContent(createInfo.fileName(), (long) bytes.length,
          createInfo.contentType(), bytes);
      user.setProfileId(profileImage.getId());
      contentRepository.save(profileImage);
    }

    // Repo 저장
    userStatusRepository.save(status);
    userRepository.save(user);
    return UserMapper.toUserResultDto(user);
  }

  @Override
  public UserResultDto findUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    return UserMapper.toUserResultDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(user -> {
          UserStatus status = userStatusRepository.findByUserId(user.getId())
              .orElseThrow(UserStatusNotFoundException::new);
          return UserMapper.toUserDto(user, status);
        })
        .toList();
  }

  public List<UserDto> findAllWithUserDTO() {
    return userRepository.findAll()
        .stream()
        .map(user -> {
          UserStatus status = userStatusRepository.findByUserId(user.getId())
              .orElseThrow(UserStatusNotFoundException::new);
          return UserMapper.toUserDto(user, status);
        })
        .toList();
  }

  @Override
  public List<UserResultDto> findAllByChannelId(UUID channelId) {
    return userRepository.findAll()
        .stream()
        .filter(user -> user.getChannelIds().contains(channelId))
        .map(UserMapper::toUserResultDto)
        .toList();
  }

  @Override
  public UserResultDto updateUser(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> image) {
    validateUserExist(request.newUsername());
    validateEmailExist(request.newEmail());
    User findUser = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    Optional.ofNullable(request.newUsername())
        .ifPresent(findUser::updateUserName);
    Optional.ofNullable(request.newPassword())
        .ifPresent(findUser::updatePassword);
    Optional.ofNullable(request.newEmail())
        .ifPresent(findUser::updateEmail);

    // profileId가 존재하면 업데이트
    if (image.isPresent()) {
      if (findUser.isProfileImageUploaded()) {
        contentRepository.deleteById(findUser.getProfileId());
      }
      BinaryContentCreateRequest createInfo = image.get();
      byte[] bytes = createInfo.bytes();
      BinaryContent profileImage = new BinaryContent(createInfo.fileName(), (long) bytes.length,
          createInfo.contentType(), bytes);
      findUser.setProfileId(profileImage.getId());
      contentRepository.save(profileImage);
    }

    // statusRepo.findByUserId로 찾기
    UserStatus status = userStatusRepository.findByUserId(findUser.getId())
        .map(findStatus -> {
          findStatus.update();
          return findStatus;
        })
        .orElseThrow(UserStatusNotFoundException::new);
    // status 업데이트 & save
    userStatusRepository.save(status);

    userRepository.save(findUser);
    return UserMapper.toUserResultDto(findUser);
  }

  @Override
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    channelRepository.findAllByUserId(userId).forEach(channel -> {
      channel.removeUserId(userId);
      channelRepository.save(channel);
    });
    if (user.isProfileImageUploaded()) {
      contentRepository.deleteById(user.getProfileId());
    }
    userStatusRepository.deleteByUserId(userId);
    userRepository.deleteById(userId);
  }

  private void validateUserExist(String userName) {
    if (userRepository.findByName(userName).isPresent()) {
      throw new UserDuplicationException();
    }
  }

  private void validateEmailExist(String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new EmailDuplicationException();
    }
  }
}
