package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class ChannelMessageController {

  private final MessageService messageService;

  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 목록 조회 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = MessageDto.class))
          )
      )
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<MessageDto>> findAllByChannelId(
      @Parameter(description = "조회할 Channel ID") @RequestParam UUID channelId
  ) {
    return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
  }
}
