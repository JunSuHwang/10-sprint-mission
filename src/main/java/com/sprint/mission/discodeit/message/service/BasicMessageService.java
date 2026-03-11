package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.global.dto.MyPageRequest;
import com.sprint.mission.discodeit.global.dto.PageResponse;
import com.sprint.mission.discodeit.global.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  private final BinaryContentStorage storage;
  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  @Override
  public MessageDto createMessage(MessageCreateRequest request,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {

    User author = userRepository.findById(request.authorId())
        .orElseThrow(UserNotFoundException::new);
    Channel findChannel = channelRepository.findById(request.channelId())
        .orElseThrow(ChannelNotFoundException::new);

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(contentRequest -> {
          BinaryContent binaryContent = binaryContentMapper.toEntity(contentRequest);
          contentRepository.save(binaryContent);
          storage.put(binaryContent.getId(), contentRequest.bytes());
          return binaryContent;
        })
        .toList();

    Message message = new Message(request.content(), findChannel, author, attachments);
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
  public PageResponse<MessageDto> findAllByChannelId(MyPageRequest<UUID> request) {
    Pageable pageable = PageRequest.of(0, request.pageable().getPageSize() + 1);
    List<Message> messages =
        request.currentCursor() == null
            ? messageRepository.findFirstPageByChannelId(request.t(), pageable)
            : messageRepository.findNextPageByChannelId(request.t(),
                (Instant) request.currentCursor(),
                pageable);
    boolean hasNext = messages.size() > request.pageable().getPageSize();

    if (hasNext) {
      messages = messages.subList(0, request.pageable().getPageSize());
    }

    Instant nextCursor = hasNext ? messages.get(messages.size() - 1).getCreatedAt() : null;
    List<MessageDto> messageDtos = toMessageDtoList(messages);

    return new PageResponse<>(messageDtos, nextCursor, messageDtos.size(), hasNext, null);
  }

  @Transactional
  @Override
  public MessageDto updateMessage(UUID messageId, MessageUpdateRequest request) {
    Message findMessage = messageRepository.findById(messageId)
        .orElseThrow(MessageNotFoundException::new);

    Optional.ofNullable(request.newContent())
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
