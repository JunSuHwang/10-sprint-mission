package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.dto.LoginRequest;
import com.sprint.mission.discodeit.user.dto.UserInfo;
import com.sprint.mission.discodeit.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "로그인")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = UserInfo.class)
          )
      ),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("User with username {username} not found")
          )
      ),
      @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Wrong password")
          )
      )
  })
  @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<UserInfo> login(@RequestBody LoginRequest loginInfo) {
    return ResponseEntity.ok(authService.login(loginInfo));
  }
}
