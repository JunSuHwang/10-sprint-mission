package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.dto.ChannelInfo;
import com.sprint.mission.discodeit.channel.dto.FindChannelInfo;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateInfo;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelInfo;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateInfo;
import com.sprint.mission.discodeit.channel.dto.PublicChannelInfo;
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
  public ResponseEntity<PublicChannelInfo> createChannel(
      @RequestBody PublicChannelCreateInfo channelInfo
  ) {
    return ResponseEntity.ok(channelService.createPublicChannel(channelInfo));
  }

  @RequestMapping(value = "/private", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<PrivateChannelInfo> createChannel(
      @RequestBody PrivateChannelCreateInfo channelInfo
  ) {
    return ResponseEntity.ok(channelService.createPrivateChannel(channelInfo));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<ChannelInfo> getChannel(@PathVariable UUID channelId) {
    return ResponseEntity.ok(channelService.findChannel(channelId));
  }

  @RequestMapping(method = RequestMethod.GET, consumes = "application/json")
  public ResponseEntity<List<ChannelInfo>> getAllVisibleChannels(
      @RequestBody FindChannelInfo findChannelInfo) {
    return ResponseEntity.ok(channelService.findAllByUserId(findChannelInfo.userId()));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH, consumes = "application/json")
  public ResponseEntity<Void> updateChannel(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelCreateInfo channelInfo
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
