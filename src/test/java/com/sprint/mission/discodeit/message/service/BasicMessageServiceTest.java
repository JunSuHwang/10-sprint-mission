package com.sprint.mission.discodeit.message.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.common.dto.MyPageRequest;
import com.sprint.mission.discodeit.common.dto.PageResponse;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private BinaryContentRepository contentRepository;

  @Mock
  private BinaryContentMapper binaryContentMapper;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private BinaryContentStorage storage;

  @InjectMocks
  private BasicMessageService messageService;

  @Test
  void createMessage_success_withoutAttachments() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("안녕하세요", authorId, channelId);
    List<BinaryContentCreateRequest> attachments = List.of();

    User author = mock(User.class);
    Channel channel = mock(Channel.class);

    MessageDto expectedDto = mock(MessageDto.class);

    when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    when(messageMapper.toDto(any(Message.class))).thenReturn(expectedDto);

    // when
    MessageDto result = messageService.createMessage(request, attachments);

    // then
    assertEquals(expectedDto, result);

    verify(userRepository).findById(authorId);
    verify(channelRepository).findById(channelId);
    verify(messageRepository).save(messageCaptor.capture());
    verify(messageMapper).toDto(any(Message.class));

    Message savedMessage = messageCaptor.getValue();
    assertEquals("안녕하세요", savedMessage.getContent());
    assertEquals(author, savedMessage.getAuthor());
    assertEquals(channel, savedMessage.getChannel());
    assertTrue(savedMessage.getAttachmentIds().isEmpty());

    verify(contentRepository, never()).save(any());
    verify(storage, never()).put(any(), any());
  }

  @Test
  void createMessage_success_withAttachments() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("파일 첨부", authorId, channelId);

    BinaryContentCreateRequest file1 =
        new BinaryContentCreateRequest("a.png", "image/png", new byte[]{1, 2});
    BinaryContentCreateRequest file2 =
        new BinaryContentCreateRequest("b.txt", "text/plain", new byte[]{3, 4});

    List<BinaryContentCreateRequest> attachmentRequests = List.of(file1, file2);

    User author = mock(User.class);
    Channel channel = mock(Channel.class);

    BinaryContent binary1 = mock(BinaryContent.class);
    BinaryContent binary2 = mock(BinaryContent.class);
    UUID binaryId1 = UUID.randomUUID();
    UUID binaryId2 = UUID.randomUUID();

    when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

    when(binaryContentMapper.toEntity(file1)).thenReturn(binary1);
    when(binaryContentMapper.toEntity(file2)).thenReturn(binary2);

    when(binary1.getId()).thenReturn(binaryId1);
    when(binary2.getId()).thenReturn(binaryId2);

    MessageDto expectedDto = mock(MessageDto.class);
    when(messageMapper.toDto(any(Message.class))).thenReturn(expectedDto);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    // when
    MessageDto result = messageService.createMessage(request, attachmentRequests);

    // then
    assertEquals(expectedDto, result);

    verify(binaryContentMapper).toEntity(file1);
    verify(binaryContentMapper).toEntity(file2);

    verify(contentRepository).save(binary1);
    verify(contentRepository).save(binary2);

    verify(storage).put(binaryId1, file1.bytes());
    verify(storage).put(binaryId2, file2.bytes());

    verify(messageRepository).save(messageCaptor.capture());

    Message savedMessage = messageCaptor.getValue();
    assertEquals(2, savedMessage.getAttachmentIds().size());
    verify(messageMapper).toDto(any(Message.class));
  }

  @Test
  void createMessage_fail_whenAuthorNotFound() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("안녕하세요", authorId, channelId);

    when(userRepository.findById(authorId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class,
        () -> messageService.createMessage(request, List.of()));

    verify(userRepository).findById(authorId);
    verify(channelRepository, never()).findById(any());
    verify(messageRepository, never()).save(any());
    verify(messageMapper, never()).toDto(any());
  }

  @Test
  void createMessage_fail_whenChannelNotFound() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("안녕하세요", authorId, channelId);

    User author = mock(User.class);

    when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ChannelNotFoundException.class,
        () -> messageService.createMessage(request, List.of()));

    verify(userRepository).findById(authorId);
    verify(channelRepository).findById(channelId);
    verify(messageRepository, never()).save(any());
    verify(messageMapper, never()).toDto(any());
  }

  @Test
  void findMessage_success() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = mock(Message.class);
    MessageDto expectedDto = mock(MessageDto.class);

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    when(messageMapper.toDto(message)).thenReturn(expectedDto);

    // when
    MessageDto result = messageService.findMessage(messageId);

    // then
    assertEquals(expectedDto, result);
    verify(messageRepository).findById(messageId);
    verify(messageMapper).toDto(message);
  }

  @Test
  void findMessage_fail_whenNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(MessageNotFoundException.class,
        () -> messageService.findMessage(messageId));

    verify(messageRepository).findById(messageId);
    verify(messageMapper, never()).toDto(any());
  }

  @Test
  void findAllByUserId_success() {
    // given
    UUID userId = UUID.randomUUID();
    Message message1 = mock(Message.class);
    Message message2 = mock(Message.class);
    MessageDto dto1 = mock(MessageDto.class);
    MessageDto dto2 = mock(MessageDto.class);

    when(messageRepository.findAllByAuthorId(userId)).thenReturn(List.of(message1, message2));
    when(messageMapper.toDto(message1)).thenReturn(dto1);
    when(messageMapper.toDto(message2)).thenReturn(dto2);

    // when
    List<MessageDto> result = messageService.findAllByUserId(userId);

    // then
    assertEquals(2, result.size());
    assertEquals(List.of(dto1, dto2), result);

    verify(messageRepository).findAllByAuthorId(userId);
    verify(messageMapper).toDto(message1);
    verify(messageMapper).toDto(message2);
  }

  @Test
  void updateMessage_success_whenNewContentExists() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = new Message("기존 내용", mock(Channel.class), mock(User.class), List.of());
    ReflectionTestUtils.setField(message, "id", messageId);

    MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");
    MessageDto expectedDto = mock(MessageDto.class);

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    when(messageMapper.toDto(message)).thenReturn(expectedDto);

    // when
    MessageDto result = messageService.updateMessage(messageId, request);

    // then
    assertEquals(expectedDto, result);
    assertEquals("수정된 내용", message.getContent());

    verify(messageRepository).findById(messageId);
    verify(messageMapper).toDto(message);
  }

  @Test
  void updateMessage_success_whenNewContentIsNull() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = new Message("기존 내용", mock(Channel.class), mock(User.class), List.of());
    ReflectionTestUtils.setField(message, "id", messageId);

    MessageUpdateRequest request = new MessageUpdateRequest(null);
    MessageDto expectedDto = mock(MessageDto.class);

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
    when(messageMapper.toDto(message)).thenReturn(expectedDto);

    // when
    MessageDto result = messageService.updateMessage(messageId, request);

    // then
    assertEquals(expectedDto, result);
    assertEquals("기존 내용", message.getContent());

    verify(messageRepository).findById(messageId);
    verify(messageMapper).toDto(message);
  }

  @Test
  void updateMessage_fail_whenMessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(MessageNotFoundException.class,
        () -> messageService.updateMessage(messageId, request));

    verify(messageRepository).findById(messageId);
    verify(messageMapper, never()).toDto(any());
  }

  @Test
  void deleteMessage_success() {
    // given
    UUID messageId = UUID.randomUUID();

    // when
    messageService.deleteMessage(messageId);

    // then
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  void findAllByChannelId_firstPage_success() {
    // given
    UUID channelId = UUID.randomUUID();

    Message message1 = mock(Message.class);
    Message message2 = mock(Message.class);
    Message message3 = mock(Message.class);

    MessageDto dto1 = mock(MessageDto.class);
    MessageDto dto2 = mock(MessageDto.class);

    Instant t2 = Instant.parse("2025-01-01T09:00:00Z");

    when(message2.getCreatedAt()).thenReturn(t2);

    MyPageRequest<UUID> request =
        new MyPageRequest<>(channelId, PageRequest.of(0, 2), null);

    when(messageRepository.findFirstPageByChannelId(eq(channelId), any(Pageable.class)))
        .thenReturn(List.of(message1, message2, message3));

    when(messageMapper.toDto(message1)).thenReturn(dto1);
    when(messageMapper.toDto(message2)).thenReturn(dto2);

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(request);

    // then
    assertEquals(2, result.content().size());
    assertEquals(List.of(dto1, dto2), result.content());
    assertEquals(t2, result.nextCursor());
    assertTrue(result.hasNext());
    assertEquals(2, result.size());

    verify(messageRepository).findFirstPageByChannelId(eq(channelId), any(Pageable.class));
  }

  @Test
  void findAllByChannelId_nextPage_success() {
    // given
    UUID channelId = UUID.randomUUID();
    Instant currentCursor = Instant.parse("2025-01-01T10:00:00Z");

    Message message1 = mock(Message.class);
    MessageDto dto1 = mock(MessageDto.class);

    MyPageRequest<UUID> request =
        new MyPageRequest<>(channelId, PageRequest.of(0, 2), currentCursor);

    when(messageRepository.findNextPageByChannelId(eq(channelId), eq(currentCursor),
        any(Pageable.class)))
        .thenReturn(List.of(message1));

    when(messageMapper.toDto(message1)).thenReturn(dto1);

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(request);

    // then
    assertEquals(1, result.content().size());
    assertEquals(List.of(dto1), result.content());
    assertNull(result.nextCursor());
    assertFalse(result.hasNext());

    verify(messageRepository).findNextPageByChannelId(eq(channelId), eq(currentCursor),
        any(Pageable.class));
  }
}