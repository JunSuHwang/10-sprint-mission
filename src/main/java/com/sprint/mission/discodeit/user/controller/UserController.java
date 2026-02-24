package com.sprint.mission.discodeit.user.controller;


import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserResultDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  public ResponseEntity<UserResultDto> getUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.findUser(userId));
  }

  @Operation(summary = "전체 User 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
          )
      )
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @Operation(summary = "User 등록",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              encoding = @Encoding(name = "userCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = UserResultDto.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("User with email {email} already exists")
          )
      )
  })
  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResultDto> create(
      @Parameter() @RequestPart UserCreateRequest userCreateRequest,
      @Parameter(description = "User 프로필 이미지") @RequestPart(required = false) MultipartFile profile
  ) {
    return ResponseEntity.status(201).body(
        userService.createUser(userCreateRequest, resolveProfileFile(profile))
    );
  }

  @Operation(summary = "User 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("User with id {id} not found"))
      )
  })
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 User ID") @PathVariable UUID userId
  ) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "User 정보 수정",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              encoding = @Encoding(name = "userUpdateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("User with id {userId} not found")
          )
      ),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("user with email {newEmail} already exists")
          )
      ),
      @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = UserResultDto.class)
          )
      )
  })
  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResultDto> update(
      @Parameter(description = "수정할 User ID") @PathVariable UUID userId,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @Parameter(description = "수정할 User 프로필 이미지") @RequestPart(required = false) MultipartFile profile

  ) {
    return ResponseEntity.ok(
        userService.updateUser(userId, userUpdateRequest, resolveProfileFile(profile)));
  }

  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("UserStatus with userId {userId} not found")
          )
      ),
      @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = UserStatusDto.class)
          )
      )
  })
  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @Parameter(description = "상태를 변경할 User ID") @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(userStatusService.update(userId, request));
  }

  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.GET)
  public ResponseEntity<UserStatusDto> getUserStatus(@PathVariable UUID userId) {
    return ResponseEntity.ok(userStatusService.findUserStatusByUserId(userId));
  }

  private Optional<BinaryContentCreateRequest> resolveProfileFile(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest contentInfo = new BinaryContentCreateRequest(
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
