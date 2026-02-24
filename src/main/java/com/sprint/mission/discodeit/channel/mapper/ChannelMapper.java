package com.sprint.mission.discodeit.channel.mapper;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.channel.dto.PublicChannelDto;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.common.ChannelType;
import java.time.Instant;

public final class ChannelMapper {

  private ChannelMapper() {
  }

  public static ChannelDto toChannelInfo(Channel channel, Instant lastMessageTime) {
    if (channel.getType() == ChannelType.PRIVATE) {
      return new ChannelDto(channel.getId(),
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

  public static PublicChannelDto toPublicChannelInfo(Channel channel) {
    return new PublicChannelDto(
        channel.getId(),
        channel.getName(),
        channel.getType(),
        channel.getDescription()
    );
  }

  public static PrivateChannelDto toPrivateChannelInfo(Channel channel) {
    return new PrivateChannelDto(
        channel.getId(),
        channel.getType()
    );
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

  public static Channel toChannel(PublicChannelDto channelInfo) {
    return new Channel(
        channelInfo.name(),
        ChannelType.PUBLIC,
        channelInfo.description()
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
