package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelDto;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.exception.ChannelDuplicationException;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.exception.ChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.common.ChannelType;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  public PublicChannelDto createPublicChannel(PublicChannelCreateRequest channelInfo) {
    validateChannelExist(channelInfo.name());
    Channel channel = new Channel(channelInfo.name(), ChannelType.PUBLIC,
        channelInfo.description());
    channelRepository.save(channel);
    return ChannelMapper.toPublicChannelInfo(channel);
  }

  public PrivateChannelDto createPrivateChannel(PrivateChannelCreateRequest channelInfo) {
    Channel channel = new Channel(null, ChannelType.PRIVATE, null);
    channelRepository.save(channel);
    channelInfo.participantIds().forEach(userId -> joinChannel(channel.getId(), userId));
    return ChannelMapper.toPrivateChannelInfo(channel);
  }

  @Override
  public ChannelDto findChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);

    return ChannelMapper.toChannelInfo(channel, getLastMessageTime(channelId));
  }

  @Override
  public List<ChannelDto> findAll() {
    return channelRepository.findAll()
        .stream()
        .map(channel ->
            ChannelMapper.toChannelInfo(channel, getLastMessageTime(channel.getId()))
        )
        .toList();
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAll()
        .stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC
            || (channel.getType() == ChannelType.PRIVATE && channel.getUserIds()
            .contains(userId)))
        .map(channel ->
            ChannelMapper.toChannelInfo(channel, getLastMessageTime(channel.getId()))
        )
        .toList();
  }

  @Override
  public ChannelDto updateChannel(UUID channelId, PublicChannelCreateRequest channelInfo) {
    Channel findChannel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    if (findChannel.getType() == ChannelType.PRIVATE) {
      throw new ChannelUpdateNotAllowedException();
    }
    validateChannelExist(channelInfo.name());

    Optional.ofNullable(channelInfo.name())
        .ifPresent(findChannel::updateChannelName);
    Optional.ofNullable(channelInfo.description())
        .ifPresent(findChannel::updateDescription);

    channelRepository.save(findChannel);

    return ChannelMapper.toChannelInfo(findChannel, getLastMessageTime(findChannel.getId()));
  }

  @Override
  public void deleteChannel(UUID channelId) {
    channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    userRepository.findAllByChannelId(channelId).forEach(user -> {
      user.removeChannelId(channelId);
      userRepository.save(user);
    });

    messageRepository.findAllByChannelId(channelId).forEach(message ->
        messageRepository.deleteById(message.getId()));

    readStatusRepository.findAllByChannelId(channelId).forEach(readStatus ->
        readStatusRepository.deleteById(readStatus.getId()));

    channelRepository.deleteById(channelId);
  }

  @Override
  public void joinChannel(UUID channelId, UUID userId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    channel.addUserId(userId);
    user.addChannelId(channelId);

    channelRepository.save(channel);
    userRepository.save(user);
    readStatusRepository.save(new ReadStatus(userId, channelId));
  }

  @Override
  public void leaveChannel(UUID channelId, UUID userId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);
    ReadStatus findReadStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new IllegalStateException("유저가 채널에 가입되어 있지 않습니다."));

    channel.removeUserId(userId);
    user.removeChannelId(channelId);

    channelRepository.save(channel);
    userRepository.save(user);
    readStatusRepository.deleteById(findReadStatus.getId());
  }

  private void validateChannelExist(String channelName) {
    if (channelRepository.findByName(channelName).isPresent()) {
      throw new ChannelDuplicationException();
    }
  }

  private Instant getLastMessageTime(UUID channelId) {
    List<Message> messages = messageRepository.findAllByChannelId(channelId);
    if (messages.isEmpty()) {
      return null;
    } else {
      return messages
          .stream()
          .max(Comparator.comparing(Message::getUpdateAt))
          .orElseThrow(() -> new IllegalStateException("메세지가 존재하지 않습니다."))
          .getUpdateAt();
    }
  }
}
