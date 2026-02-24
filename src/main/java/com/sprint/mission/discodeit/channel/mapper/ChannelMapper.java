package com.sprint.mission.discodeit.channel.mapper;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.ChannelResultDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.common.ChannelType;
import java.time.Instant;

public final class ChannelMapper {

  private ChannelMapper() {
  }

  public static ChannelDto toChannelDto(Channel channel, Instant lastMessageTime) {
    if (channel.getType() == ChannelType.PRIVATE) {
      return new ChannelDto(
          channel.getId(),
          null,
          channel.getType(),
          null,
          lastMessageTime,
          channel.getUserIds());
    } else {
      return new ChannelDto(
          channel.getId(),
          channel.getName(),
          channel.getType(),
          channel.getDescription(),
          null,
          channel.getUserIds()
      );
    }
  }

  public static ChannelResultDto toChannelResultDto(Channel channel) {
    if (channel.getType() == ChannelType.PRIVATE) {
      return new ChannelResultDto(
          channel.getId(),
          channel.getCreatedAt(),
          channel.getUpdatedAt(),
          channel.getType(),
          null,
          null
      );
    } else {
      return new ChannelResultDto(
          channel.getId(),
          channel.getCreatedAt(),
          channel.getUpdatedAt(),
          channel.getType(),
          channel.getName(),
          channel.getDescription()
      );
    }
  }

  public static PrivateChannelCreateRequest toPrivateChannelCreateInfo(Channel channel) {
    return new PrivateChannelCreateRequest(channel.getUserIds());
  }

  public static Channel toChannel(ChannelDto channelDto) {
    return new Channel(
        channelDto.name(),
        channelDto.type(),
        channelDto.description()
    );
  }

  public static Channel toChannel(PrivateChannelCreateRequest channelInfo) {
    return new Channel(
        null,
        ChannelType.PRIVATE,
        null
    );
  }
}
