package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository contentRepository;
  private final MessageMapper messageMapper;

  @Transactional
  @Override
  public MessageDto createMessage(MessageCreateRequest createInfo,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {

    User author = userRepository.findById(createInfo.authorId())
        .orElseThrow(UserNotFoundException::new);
    Channel findChannel = channelRepository.findById(createInfo.channelId())
        .orElseThrow(ChannelNotFoundException::new);

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(request -> {
          BinaryContent binaryContent = new BinaryContent(
              request.fileName(),
              (long) request.bytes().length,
              request.contentType(),
              request.bytes()
          );
          contentRepository.save(binaryContent);
          return binaryContent;
        })
        .toList();

    Message message = new Message(createInfo.content(), findChannel, author, attachments);
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @Override
  public MessageDto findMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(MessageNotFoundException::new);

    return messageMapper.toDto(message);
  }

  @Override
  public List<MessageDto> findAll() {
    return toMessageDtoList(messageRepository.findAll());
  }

  @Override
  public List<MessageDto> findAllByUserId(UUID userId) {
    return toMessageDtoList(messageRepository.findAllByAuthorId(userId));
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return toMessageDtoList(messageRepository.findAllByChannelId(channelId));
  }

  @Transactional
  @Override
  public MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageInfo) {
    Message findMessage = messageRepository.findById(messageId)
        .orElseThrow(MessageNotFoundException::new);

    Optional.ofNullable(messageInfo.newContent())
        .ifPresent(findMessage::update);

    return messageMapper.toDto(findMessage);
  }

  @Transactional
  @Override
  public void deleteMessage(UUID messageId) {
    messageRepository.deleteById(messageId);
  }

  private List<MessageDto> toMessageDtoList(List<Message> messages) {
    return messages.stream()
        .map(messageMapper::toDto)
        .toList();
  }
}
