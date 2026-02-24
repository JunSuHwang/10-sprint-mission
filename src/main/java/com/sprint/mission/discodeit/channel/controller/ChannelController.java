package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.ChannelResultDto;
import com.sprint.mission.discodeit.channel.service.ChannelService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "Public Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Public Channel이 성공적으로 생성됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = ChannelResultDto.class)
          )
      )
  })
  @RequestMapping(value = "/public", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<ChannelResultDto> create_3(
      @RequestBody PublicChannelCreateRequest channelInfo
  ) {
    return ResponseEntity.ok(channelService.createPublicChannel(channelInfo));
  }

  @Operation(summary = "Private Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Private Channel이 성공적으로 생성됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = ChannelResultDto.class)
          )
      )
  })
  @RequestMapping(value = "/private", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<ChannelResultDto> create_4(
      @RequestBody PrivateChannelCreateRequest channelInfo
  ) {
    return ResponseEntity.ok(channelService.createPrivateChannel(channelInfo));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<ChannelDto> getChannel(@PathVariable UUID channelId) {
    return ResponseEntity.ok(channelService.findChannel(channelId));
  }

  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel 목록 조회 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class))
          )
      )
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ChannelDto>> findAll_1(
      @Parameter(description = "조회할 User ID") @RequestParam UUID userId
  ) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }

  @Operation(summary = "Channel 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Channel with id {channelId} not found")
          )
      ),
      @ApiResponse(
          responseCode = "400", description = "Private Channel은 수정할 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Private channel cannot be updated")
          )
      ),
      @ApiResponse(
          responseCode = "200", description = "Channel 정보가 성공적으로 수정됨",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = ChannelResultDto.class)
          )
      )
  })
  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH, consumes = "application/json")
  public ResponseEntity<ChannelResultDto> update_3(
      @Parameter(description = "수정할 Channel ID") @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest channelInfo
  ) {
    return ResponseEntity.ok(channelService.updateChannel(channelId, channelInfo));
  }

  @Operation(summary = "Channel 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "404", description = "Channel을 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("Channel with id {channelId} not found")
          )
      ),
      @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨")
  })
  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete_2(
      @Parameter(description = "삭제할 Channel ID") @PathVariable UUID channelId
  ) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.noContent().build();
  }
}
