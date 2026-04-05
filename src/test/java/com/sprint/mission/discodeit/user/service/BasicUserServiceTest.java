package com.sprint.mission.discodeit.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.EmailDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserDuplicationException;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserStatusRepository userStatusRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService userService;

  @Test
  void createUser() {
    // given
    String username = "유저1";
    String password = "qwer123";
    String email = "email1@google.com";
    UUID userId = UUID.randomUUID();

    User user = new User(username, password, email);
    ReflectionTestUtils.setField(user, "id", userId);

    UserCreateRequest request = new UserCreateRequest(username, password, email);
    UserDto expectedDto = new UserDto(userId, username, email, null, true);

    when(userRepository.existsUserByUsername(username)).thenReturn(false);
    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(userMapper.toEntity(request)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(expectedDto);

    // when
    UserDto dto = userService.createUser(request, Optional.empty());

    // then
    assertEquals(username, dto.username());
    assertEquals(email, dto.email());
    assertEquals(userId, dto.id());

    verify(userRepository).existsUserByUsername(username);
    verify(userRepository).existsByEmail(email);
    verify(userRepository).save(user);
    verify(userMapper).toEntity(request);
    verify(userMapper).toDto(user);
  }

  @Test
  void createUser_UsernameExists() {
    // given
    String username = "유저1";
    String password = "qwer123";
    String email = "email1@google.com";

    UserCreateRequest request = new UserCreateRequest(username, password, email);

    when(userRepository.existsUserByUsername(username)).thenReturn(true);

    // when & then
    assertThrows(UserDuplicationException.class,
        () -> userService.createUser(request, Optional.empty()));

    verify(userRepository).existsUserByUsername(username);
    verify(userRepository, never()).existsByEmail(anyString());
    verify(userRepository, never()).save(any(User.class));
    verify(userMapper, never()).toEntity(any(UserCreateRequest.class));
    verify(userMapper, never()).toDto(any(User.class));
  }

  @Test
  void updateUser() {
    // given
    UUID userId = UUID.randomUUID();
    String oldUsername = "유저1";
    String oldPassword = "qwer123";
    String oldEmail = "old@google.com";

    String newUsername = "수정유저";
    String newEmail = "new@google.com";

    User user = new User(oldUsername, oldPassword, oldEmail);
    ReflectionTestUtils.setField(user, "id", userId);

    UserUpdateRequest request = new UserUpdateRequest(newUsername, null, newEmail);
    UserDto expectedDto = new UserDto(userId, newUsername, newEmail, null, true);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.toDto(user)).thenReturn(expectedDto);
    when(userStatusRepository.findByUserId(userId)).thenReturn(Optional.empty());

    // when
    UserDto dto = userService.updateUser(userId, request, Optional.empty());

    // then
    assertEquals(newUsername, dto.username());
    assertEquals(newEmail, dto.email());
    assertEquals(userId, dto.id());

    verify(userRepository).findById(userId);
    verify(userMapper).update(request, user);
    verify(userMapper).toDto(user);
  }

  @Test
  void updateUser_EmailExists() {
    // given
    UUID userId = UUID.randomUUID();

    User user = new User("기존유저", "qwer123", "old@google.com");
    ReflectionTestUtils.setField(user, "id", userId);

    UserUpdateRequest request = new UserUpdateRequest("수정유저", null, "duplicate@google.com");

    when(userRepository.existsUserByUsername("수정유저")).thenReturn(false);
    when(userRepository.existsByEmail("duplicate@google.com")).thenReturn(true);

    // when & then
    assertThrows(EmailDuplicationException.class,
        () -> userService.updateUser(userId, request, Optional.empty()));

    verify(userRepository).existsUserByUsername("수정유저");
    verify(userRepository).existsByEmail("duplicate@google.com");
    verify(userRepository, never()).save(any(User.class));
    verify(userMapper, never()).toDto(any(User.class));
  }

  @Test
  void deleteUser() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("유저1", "qwer123", "email1@google.com");
    ReflectionTestUtils.setField(user, "id", userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // when
    userService.deleteUser(userId);

    // then
    verify(userRepository).findById(userId);
    verify(userRepository).deleteById(userId);
  }

  @Test
  void deleteUser_UserNotFound() {
    // given
    UUID userId = UUID.randomUUID();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class,
        () -> userService.deleteUser(userId));

    verify(userRepository).findById(userId);
    verify(userRepository, never()).delete(any(User.class));
  }
}