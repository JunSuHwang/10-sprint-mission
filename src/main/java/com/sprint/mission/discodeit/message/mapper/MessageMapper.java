package com.sprint.mission.discodeit.message.mapper;

import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface MessageMapper {

  @Mapping(target = "channelId", source = "channel.id")
  MessageDto toDto(Message message);
}
