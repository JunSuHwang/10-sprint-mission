package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.user.entity.Role;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) {
    if (userRepository.existsUserByUsername("admin")) {
      return;
    }
    User admin = new User(
        "admin",
        passwordEncoder.encode("admin1234"),
        "admin@email.com"
    );
    admin.setRole(Role.ADMIN);
    userRepository.save(admin);
  }
}
