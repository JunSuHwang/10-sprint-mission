package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.base.BaseEntity;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.exception.ChannelDuplicationException;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.exception.ChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserMapper userMapper;
  private final ChannelMapper channelMapper;

  @Transactional
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    validateChannelExist(request.name());
    Channel channel = new Channel(request.name(), ChannelType.PUBLIC,
        request.description());
    channelRepository.save(channel);
    return channelMapper.toDto(channel,
        getLastMessageAt(channel.getId()), getParticipants(channel.getId()));
  }

  @Transactional
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(null, ChannelType.PRIVATE, null);
    channelRepository.save(channel);
    request.participantIds().forEach(userId -> joinChannel(channel.getId(), userId));
    return channelMapper.toDto(channel,
        getLastMessageAt(channel.getId()), getParticipants(channel.getId()));
  }

  @Override
  public ChannelDto findChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    return channelMapper.toDto(channel,
        getLastMessageAt(channel.getId()), getParticipants(channel.getId()));
  }

  @Override
  public List<ChannelDto> findAll() {
    return channelRepository.findAll()
        .stream()
        .map(channel -> channelMapper.toDto(channel,
            getLastMessageAt(channel.getId()), getParticipants(channel.getId())))
        .toList();
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAll()
        .stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC
            || readStatusRepository.existsByUserIdAndChannelId(userId, channel.getId()))
        .map(channel -> channelMapper.toDto(channel,
            getLastMessageAt(channel.getId()), getParticipants(channel.getId())))
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto updateChannel(UUID channelId,
      PublicChannelUpdateRequest request) {
    Channel findChannel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    if (findChannel.getType() == ChannelType.PRIVATE) {
      throw new ChannelUpdateNotAllowedException();
    }
    Optional.ofNullable(request.newName())
        .ifPresent(this::validateChannelExist);

    Optional.ofNullable(request.newName())
        .ifPresent(findChannel::updateChannelName);
    Optional.ofNullable(request.newDescription())
        .ifPresent(findChannel::updateDescription);

    return channelMapper.toDto(findChannel,
        getLastMessageAt(findChannel.getId()), getParticipants(findChannel.getId()));
  }

  @Transactional
  @Override
  public void deleteChannel(UUID channelId) {
    channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    channelRepository.deleteById(channelId);
  }

  @Transactional
  @Override
  public void joinChannel(UUID channelId, UUID userId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    readStatusRepository.save(new ReadStatus(user, channel));
  }

  @Transactional
  @Override
  public void leaveChannel(UUID channelId, UUID userId) {
    channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    ReadStatus findReadStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(ReadStatusNotFoundException::new);

    readStatusRepository.deleteById(findReadStatus.getId());
  }

  private void validateChannelExist(String name) {
    if (channelRepository.existsByName(name)) {
      throw new ChannelDuplicationException();
    }
  }

  private Instant getLastMessageAt(UUID channelId) {
    return messageRepository.findFirstByChannel_IdOrderByCreatedAtAsc(channelId)
        .map(BaseEntity::getCreatedAt)
        .orElse(null);
  }

  private List<UserDto> getParticipants(UUID channelId) {
    return readStatusRepository.findAllByChannelId(channelId)
        .stream()
        .map(readStatus ->
            userMapper.toDto(readStatus.getUser())
        )
        .toList();
  }
}
