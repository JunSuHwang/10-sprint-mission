package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ApiUserController {

  private final UserService userService;

  @RequestMapping(value = "/findAll", method = RequestMethod.GET)
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAllWithUserDTO());
  }
}
