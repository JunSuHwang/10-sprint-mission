package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageCreateRequest messageInfo) {
    return ResponseEntity.ok(messageService.createMessage(messageInfo));
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
