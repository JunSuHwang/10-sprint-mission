package com.sprint.mission.discodeit.channel.mapper;

import com.sprint.mission.discodeit.base.BaseEntity;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

  @Autowired
  protected MessageRepository messageRepository;

  @Autowired
  protected ReadStatusRepository readStatusRepository;

  @Autowired
  protected UserMapper userMapper;

  @Mapping(target = "lastMessageAt", expression = "java(getLastMessageAt(channel.getId()))")
  @Mapping(target = "participants", expression = "java(getParticipants(channel.getId()))")
  public abstract ChannelDto toDto(Channel channel);

  protected Instant getLastMessageAt(UUID channelId) {
    return messageRepository.findFirstByChannel_IdOrderByCreatedAtAsc(channelId)
        .map(BaseEntity::getCreatedAt)
        .orElse(null);
  }

  protected List<UserDto> getParticipants(UUID channelId) {
    return readStatusRepository.findAllByChannelId(channelId)
        .stream()
        .map(readStatus ->
            userMapper.toDto(readStatus.getUser())
        )
        .toList();
  }
}
