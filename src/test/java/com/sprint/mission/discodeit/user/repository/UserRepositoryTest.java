package com.sprint.mission.discodeit.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("username으로 사용자를 조회할 수 있다")
  void findByUsername_success() {
    // given
    User user = new User("user1", "qwer123", "user1@gmail.com");
    userRepository.save(user);

    // when
    Optional<User> result = userRepository.findByUsername("user1");

    // then
    assertTrue(result.isPresent());
    assertEquals("user1", result.get().getUsername());
    assertEquals("user1@gmail.com", result.get().getEmail());
  }

  @Test
  @DisplayName("존재하는 username이면 true를 반환한다")
  void existsUserByUsername_returnsTrue() {
    // given
    User user = new User("user1", "qwer123", "user1@gmail.com");
    userRepository.save(user);

    // when
    boolean result = userRepository.existsUserByUsername("user1");

    // then
    assertTrue(result);
  }

  @Test
  @DisplayName("존재하지 않는 username이면 false를 반환한다")
  void existsUserByUsername_returnsFalse() {
    // given
    User user = new User("user1", "qwer123", "user1@gmail.com");
    userRepository.save(user);

    // when
    boolean result = userRepository.existsUserByUsername("user2");

    // then
    assertFalse(result);
  }

  @Test
  @DisplayName("존재하는 email이면 true를 반환한다")
  void existsByEmail_returnsTrue() {
    // given
    User user = new User("user1", "qwer123", "user1@gmail.com");
    userRepository.save(user);

    // when
    boolean result = userRepository.existsByEmail("user1@gmail.com");

    // then
    assertTrue(result);
  }

  @Test
  @DisplayName("존재하지 않는 email이면 false를 반환한다")
  void existsByEmail_returnsFalse() {
    // given
    User user = new User("user1", "qwer123", "user1@gmail.com");
    userRepository.save(user);

    // when
    boolean result = userRepository.existsByEmail("user2@gmail.com");

    // then
    assertFalse(result);
  }
}