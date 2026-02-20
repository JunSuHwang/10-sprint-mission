package com.sprint.mission.discodeit.user.controller;


import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateInfo;
import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.user.dto.UserCreateInfo;
import com.sprint.mission.discodeit.user.dto.UserInfo;
import com.sprint.mission.discodeit.user.dto.UserInfoWithStatus;
import com.sprint.mission.discodeit.user.dto.UserUpdateInfo;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusInfo;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  public ResponseEntity<UserInfoWithStatus> getUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.findUser(userId));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserInfoWithStatus>> getAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserInfo> createUser(
      @RequestPart UserCreateInfo createInfo,
      @RequestPart MultipartFile image
  ) {
    return ResponseEntity.ok(userService.createUser(createInfo, resolveProfileFile(image)));
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> updateUser(
      @PathVariable UUID userId,
      @RequestPart UserUpdateInfo updateInfo,
      @RequestPart MultipartFile image

  ) {
    userService.updateUser(userId, updateInfo, resolveProfileFile(image));
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatusInfo> updateUserStatusByUserId(@PathVariable UUID userId) {
    return ResponseEntity.ok(userStatusService.updateUserStatusByUserId(userId));
  }

  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.GET)
  public ResponseEntity<UserStatusInfo> getUserStatus(@PathVariable UUID userId) {
    return ResponseEntity.ok(userStatusService.findUserStatusByUserId(userId));
  }

  private Optional<BinaryContentCreateInfo> resolveProfileFile(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateInfo contentInfo = new BinaryContentCreateInfo(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(contentInfo);
      } catch (IOException e) {
        throw new BinaryContentNotFoundException();
      }
    }
  }
}
