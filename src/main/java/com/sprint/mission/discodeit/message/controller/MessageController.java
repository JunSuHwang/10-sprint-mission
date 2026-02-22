package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> sendMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments) {
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
                throw new BinaryContentNotFoundException();
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    return ResponseEntity.ok(
        messageService.createMessage(messageCreateRequest, attachmentRequests));
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
  public ResponseEntity<MessageDto> getMessage(@PathVariable UUID messageId) {
    return ResponseEntity.ok(messageService.findMessage(messageId));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<MessageDto>> getAllMessages() {
    return ResponseEntity.ok(messageService.findAll());
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<Void> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageInfo
  ) {
    messageService.updateMessage(messageId, messageInfo);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.noContent().build();
  }
}
