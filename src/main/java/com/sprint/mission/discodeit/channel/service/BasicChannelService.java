package com.sprint.mission.discodeit.channel.service;

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
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    log.debug("[CHANNEL_CREATE] PUBLIC 채널 생성 시작 name={}", request.name());

    validateChannelExist(request.name());
    Channel channel = channelMapper.toEntity(request);
    channelRepository.save(channel);
    log.info("[CHANNEL_CREATE] PUBLIC 채널 생성 id={}", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Transactional
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    log.debug("[CHANNEL_CREATE] PRIVATE 채널 생성 시작");

    Channel channel = channelMapper.toEntity(request);
    channelRepository.save(channel);
    request.participantIds().forEach(
        userId -> {
          User findUser = userRepository.findById(userId)
              .orElseThrow(() -> UserNotFoundException.ById(userId));
          readStatusRepository.save(new ReadStatus(findUser, channel, channel.getCreatedAt()));
        });
    log.info("[CHANNEL_CREATE] PRIVATE 채널 생성 id={}", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto findChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
    return channelMapper.toDto(channel);
  }

  @Override
  public List<ChannelDto> findAll() {
    return channelRepository.findAll()
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAll()
        .stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC
            || readStatusRepository.existsByUserIdAndChannelId(userId, channel.getId()))
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto updateChannel(UUID channelId,
      PublicChannelUpdateRequest request) {
    log.debug("[CHANNEL_UPDATE] 채널 수정 시작 id={}", channelId);
    Channel findChannel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
    if (findChannel.getType() == ChannelType.PRIVATE) {
      throw new ChannelUpdateNotAllowedException(channelId);
    }
    channelMapper.update(request, findChannel);

    log.info("[CHANNEL_UPDATE] 채널 수정 id={}", findChannel.getId());

    return channelMapper.toDto(findChannel);
  }

  @Transactional
  @Override
  public void deleteChannel(UUID channelId) {
    log.debug("[CHANNEL_DELETE] 채널 삭제 시작 id={}", channelId);

    channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
    channelRepository.deleteById(channelId);

    log.info("[CHANNEL_DELETE] 채널 삭제 id={}", channelId);
  }

  private void validateChannelExist(String name) {
    if (channelRepository.existsByName(name)) {
      throw new ChannelDuplicationException(name);
    }
  }
}
