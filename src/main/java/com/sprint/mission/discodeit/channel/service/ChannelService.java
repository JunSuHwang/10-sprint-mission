package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  PublicChannelDto createPublicChannel(PublicChannelCreateRequest channelInfo);

  PrivateChannelDto createPrivateChannel(PrivateChannelCreateRequest channelInfo);

  ChannelDto findChannel(UUID channelId);

  List<ChannelDto> findAll();

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto updateChannel(UUID channelId, PublicChannelCreateRequest channelInfo);

  void deleteChannel(UUID channelId);

  void joinChannel(UUID channelId, UUID userId);

  void leaveChannel(UUID channelId, UUID userId);
}
