package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateInfo;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.user.dto.*;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.EmailDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository contentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserInfo createUser(UserCreateInfo userInfo, Optional<BinaryContentCreateInfo> image) {
        // 유저 이름 & 이메일 검증
        validateUserExist(userInfo.userName());
        validateEmailExist(userInfo.email());

        // 유저 생성 -> mapper로 대체 가능
        User user = new User(userInfo.userName(), userInfo.password(), userInfo.email());

        // status 생성
        UserStatus status = new UserStatus(user.getId());

        // profile image가 존재한다면 생성
        if(image.isPresent()) {
            BinaryContentCreateInfo createInfo = image.get();
            BinaryContent profileImage = new BinaryContent(createInfo.fileName(), createInfo.contentType(), createInfo.content());
            user.setProfileId(profileImage.getId());
            contentRepository.save(profileImage);
        }

        // Repo 저장
        userStatusRepository.save(status);
        userRepository.save(user);
        return UserMapper.toUserInfo(user, status);
    }

    @Override
    public UserInfoWithStatus findUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(UserStatusNotFoundException::new);
        return UserMapper.toUserInfoWithStatus(user, status);
    }

    @Override
    public List<UserInfoWithStatus> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(UserStatusNotFoundException::new);
                    return UserMapper.toUserInfoWithStatus(user, status);
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
    public List<UserInfoWithStatus> findAllByChannelId(UUID channelId) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getChannelIds().contains(channelId))
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(UserStatusNotFoundException::new);
                    return UserMapper.toUserInfoWithStatus(user, status);
                })
                .toList();
    }

    @Override
    public UserInfo updateUser(UUID userId, UserUpdateInfo updateInfo, Optional<BinaryContentCreateInfo> image) {
        validateUserExist(updateInfo.userName());
        validateEmailExist(updateInfo.email());
        User findUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Optional.ofNullable(updateInfo.userName())
                .ifPresent(findUser::updateUserName);
        Optional.ofNullable(updateInfo.password())
                .ifPresent(findUser::updatePassword);
        Optional.ofNullable(updateInfo.email())
                .ifPresent(findUser::updateEmail);

        // profileId가 존재하면 업데이트
        if(image.isPresent()) {
            if(findUser.isProfileImageUploaded())
                contentRepository.deleteById(findUser.getProfileId());
            BinaryContentCreateInfo createInfo = image.get();
            BinaryContent profileImage = new BinaryContent(createInfo.fileName(), createInfo.contentType(), createInfo.content());
            findUser.setProfileId(profileImage.getId());
            contentRepository.save(profileImage);
        }

        // statusRepo.findByUserId로 찾기
        UserStatus status = userStatusRepository.findByUserId(findUser.getId())
                .map(findStatus -> {
                    findStatus.updateLastOnlineAt();
                    return findStatus;
                })
                .orElseThrow(UserStatusNotFoundException::new);
        // status 업데이트 & save
        userStatusRepository.save(status);

        userRepository.save(findUser);
        return UserMapper.toUserInfo(findUser, status);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        channelRepository.findAllByUserId(userId).forEach(channel -> {
            channel.removeUserId(userId);
            channelRepository.save(channel);
        });
        if(user.isProfileImageUploaded())
            contentRepository.deleteById(user.getProfileId());
        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    private void validateUserExist(String userName) {
        if(userRepository.findByName(userName).isPresent())
            throw new UserDuplicationException();
    }

    private void validateEmailExist(String email) {
        if(userRepository.findByEmail(email).isPresent())
            throw new EmailDuplicationException();
    }
}
