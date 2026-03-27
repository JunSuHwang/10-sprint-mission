package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentFileProcessingException;
import com.sprint.mission.discodeit.common.dto.MyPageRequest;
import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "Message 생성",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              encoding = @Encoding(name = "messageCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Channel | Author with id {channelId | authorId} not found")
          )
      ),
      @ApiResponse(
          responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = MessageDto.class)
          )
      )
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create_2(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @Parameter(description = "Message 첨부 파일들") @RequestPart(required = false) List<MultipartFile> attachments) {
    log.info("[API] POST /api/messages authorId={}, channelId={}", messageCreateRequest.authorId(),
        messageCreateRequest.channelId());

    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new BinaryContentFileProcessingException();
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    return ResponseEntity.status(201).body(
        messageService.createMessage(messageCreateRequest, attachmentRequests)
    );
  }

  @GetMapping(value = "/{messageId}")
  public ResponseEntity<MessageDto> getMessage(@PathVariable UUID messageId) {
    return ResponseEntity.ok(messageService.findMessage(messageId));
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<MessageDto>> getAllMessages() {
    return ResponseEntity.ok(messageService.findAll());
  }

  @Operation(summary = "Message 내용 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = MessageDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Message with id {messageId} not found")
          )
      )
  })
  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageDto> update_2(
      @Parameter(description = "수정할 Message ID") @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageInfo
  ) {
    log.info("[API] PATCH /api/messages id={}", messageId);
    return ResponseEntity.ok(messageService.updateMessage(messageId, messageInfo));
  }

  @Operation(summary = "Message 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(
          responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Message with id {messageId} not found")
          )
      )
  })
  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> delete_1(
      @Parameter(description = "삭제할 Message ID") @PathVariable UUID messageId
  ) {
    log.info("[API] DELETE /api/messages id={}", messageId);
    messageService.deleteMessage(messageId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 목록 조회 성공",
          content = @Content(mediaType = MediaType.ALL_VALUE)
      )
  })
  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @Parameter(description = "조회할 Channel ID") @RequestParam UUID channelId,
      @Parameter(description = "페이징 커서 정보") @RequestParam(required = false) Instant cursor,
      @ParameterObject @PageableDefault(size = 50, sort =
          "createdAt", direction = Direction.DESC) Pageable pageable
  ) {
    MyPageRequest<UUID> messagePagingRequest = new MyPageRequest<>(channelId, pageable, cursor);
    return ResponseEntity.status(200).body(messageService.findAllByChannelId(messagePagingRequest));
  }
}
