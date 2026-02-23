package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.FindChannelDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelDto;
import com.sprint.mission.discodeit.channel.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.service.ChannelService;
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
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;

  @RequestMapping(value = "/public", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<PublicChannelDto> createChannel(
      @RequestBody PublicChannelCreateRequest channelInfo
  ) {
    return ResponseEntity.ok(channelService.createPublicChannel(channelInfo));
  }

  @RequestMapping(value = "/private", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<PrivateChannelDto> createChannel(
      @RequestBody PrivateChannelCreateRequest channelInfo
  ) {
    return ResponseEntity.ok(channelService.createPrivateChannel(channelInfo));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<ChannelDto> getChannel(@PathVariable UUID channelId) {
    return ResponseEntity.ok(channelService.findChannel(channelId));
  }

  @RequestMapping(method = RequestMethod.GET, consumes = "application/json")
  public ResponseEntity<List<ChannelDto>> getAllVisibleChannels(
      @RequestBody FindChannelDto findChannelDto) {
    return ResponseEntity.ok(channelService.findAllByUserId(findChannelDto.userId()));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH, consumes = "application/json")
  public ResponseEntity<Void> updateChannel(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest channelInfo
  ) {
    channelService.updateChannel(channelId, channelInfo);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.noContent().build();
  }
}
