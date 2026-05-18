package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String tokenValue = csrfToken.getToken();
    log.info("CSRF 토큰 요청: {}", tokenValue);
    return ResponseEntity
        .status(203)
        .header("X-XSRF-TOKEN", tokenValue)
        .build();
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> authMe(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
    if (userDetails == null) {
      return ResponseEntity.status(401).build();
    }
    log.info("[auth/me] userDto name: {}, email: {}, profile: {}",
        userDetails.getUserDto().username(), userDetails.getUserDto().email(),
        userDetails.getUserDto().profile());
    return ResponseEntity.ok(userDetails.getUserDto());
  }
}
