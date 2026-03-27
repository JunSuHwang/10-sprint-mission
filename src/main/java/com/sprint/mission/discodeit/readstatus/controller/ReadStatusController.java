package com.sprint.mission.discodeit.readstatus.controller;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Channel | User with id {channelId | userId} not found"))
      ),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("ReadStatus with userId {userId} and channelId {channelId} already exists"))
      ),
      @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = ReadStatusDto.class))
      )
  })
  @PostMapping(consumes = "application/json")
  public ResponseEntity<ReadStatusDto> create_1(
      @RequestBody ReadStatusCreateRequest statusInfo) {
    return ResponseEntity.status(201).body(readStatusService.createReadStatus(statusInfo));
  }

  @PatchMapping(value = "/{statusId}/updated-at")
  public ResponseEntity<Void> updateReadStatus(@PathVariable UUID statusId) {
    readStatusService.updateReadStatus(statusId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = ReadStatusDto.class)
          )
      ),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("ReadStatus with id {readStatusId} not found")
          )
      )
  })
  @PatchMapping(value = "/{readStatusId}", consumes = "application/json")
  public ResponseEntity<ReadStatusDto> update_1(
      @Parameter(description = "수정할 읽음 상태 ID") @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    return ResponseEntity.ok(readStatusService.updateReadStatus(readStatusId, request));
  }

  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ReadStatusDto.class)))
      )
  })
  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
      @Parameter(description = "조회할 User ID") @RequestParam UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<ReadStatusDto>> getAllReadStatuses() {
    return ResponseEntity.ok(readStatusService.findAll());
  }

  @GetMapping(value = "/{statusId}")
  public ResponseEntity<ReadStatusDto> getReadStatus(@PathVariable UUID statusId) {
    return ResponseEntity.ok(readStatusService.find(statusId));
  }
}
