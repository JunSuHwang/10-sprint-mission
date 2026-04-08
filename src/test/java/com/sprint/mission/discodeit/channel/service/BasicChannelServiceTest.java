package com.sprint.mission.discodeit.channel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService channelService;

  @Test
  void createPublicChannel_success() {
    // given
    String channelName = "공지방";
    String description = "설명";
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(channelName, description);

    UUID channelId = UUID.randomUUID();
    Channel channel = mock(Channel.class);
    ChannelDto expectedDto = mock(ChannelDto.class);

    when(channelRepository.existsByName(channelName)).thenReturn(false);
    when(channelMapper.toEntity(request)).thenReturn(channel);
    when(channel.getId()).thenReturn(channelId);
    when(channelMapper.toDto(channel)).thenReturn(expectedDto);

    // when
    ChannelDto result = channelService.createPublicChannel(request);

    // then
    assertEquals(expectedDto, result);
    verify(channelRepository).existsByName(channelName);
    verify(channelMapper).toEntity(request);
    verify(channelRepository).save(channel);
    verify(channelMapper).toDto(channel);
  }

  @Test
  void createPublicChannel_fail_whenNameAlreadyExists() {
    // given
    String channelName = "공지방";
    String description = "설명";
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(channelName, description);

    when(channelRepository.existsByName(channelName)).thenReturn(true);

    // when & then
    assertThrows(ChannelDuplicationException.class,
        () -> channelService.createPublicChannel(request));

    verify(channelRepository).existsByName(channelName);
    verify(channelMapper, never()).toEntity((PublicChannelCreateRequest) any());
    verify(channelRepository, never()).save(any());
    verify(channelMapper, never()).toDto(any());
  }

  @Test
  void createPrivateChannel_success() {
    // given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    List<UUID> participantIds = List.of(userId1, userId2);

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    UUID channelId = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Channel channel = mock(Channel.class);
    User user1 = mock(User.class);
    User user2 = mock(User.class);
    ChannelDto expectedDto = mock(ChannelDto.class);

    when(channelMapper.toEntity(request)).thenReturn(channel);
    when(channel.getId()).thenReturn(channelId);
    when(channel.getCreatedAt()).thenReturn(createdAt);

    when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
    when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));

    when(channelMapper.toDto(channel)).thenReturn(expectedDto);

    // when
    ChannelDto result = channelService.createPrivateChannel(request);

    // then
    assertEquals(expectedDto, result);

    verify(channelMapper).toEntity(request);
    verify(channelRepository).save(channel);

    verify(userRepository).findById(userId1);
    verify(userRepository).findById(userId2);

    verify(readStatusRepository, times(2)).save(any(ReadStatus.class));
    verify(channelMapper).toDto(channel);
  }

  @Test
  void createPrivateChannel_fail_whenParticipantNotFound() {
    // given
    UUID userId1 = UUID.randomUUID();
    List<UUID> participantIds = List.of(userId1);

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    Channel channel = mock(Channel.class);
    when(channelMapper.toEntity(request)).thenReturn(channel);
    when(userRepository.findById(userId1)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class,
        () -> channelService.createPrivateChannel(request));

    verify(channelMapper).toEntity(request);
    verify(channelRepository).save(channel);
    verify(userRepository).findById(userId1);
    verify(readStatusRepository, never()).save(any());
    verify(channelMapper, never()).toDto(any());
  }

  @Test
  void updateChannel_success() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("수정된 채널명", null);

    Channel channel = mock(Channel.class);
    ChannelDto expectedDto = mock(ChannelDto.class);

    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(channel.getType()).thenReturn(ChannelType.PUBLIC);
    when(channel.getId()).thenReturn(channelId);
    when(channelMapper.toDto(channel)).thenReturn(expectedDto);

    doNothing().when(channelMapper).update(request, channel);

    // when
    ChannelDto result = channelService.updateChannel(channelId, request);

    // then
    assertEquals(expectedDto, result);
    verify(channelRepository).findById(channelId);
    verify(channelMapper).update(request, channel);
    verify(channelMapper).toDto(channel);
  }

  @Test
  void updateChannel_fail_whenChannelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("수정된 채널명", null);

    when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ChannelNotFoundException.class,
        () -> channelService.updateChannel(channelId, request));

    verify(channelRepository).findById(channelId);
    verify(channelMapper, never()).update(any(), any());
    verify(channelMapper, never()).toDto(any());
  }

  @Test
  void updateChannel_fail_whenPrivateChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("수정된 채널명", null);

    Channel channel = mock(Channel.class);

    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(channel.getType()).thenReturn(ChannelType.PRIVATE);

    // when & then
    assertThrows(ChannelUpdateNotAllowedException.class,
        () -> channelService.updateChannel(channelId, request));

    verify(channelRepository).findById(channelId);
    verify(channelMapper, never()).update(any(), any());
    verify(channelMapper, never()).toDto(any());
  }

  @Test
  void deleteChannel_success() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel channel = mock(Channel.class);

    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

    // when
    channelService.deleteChannel(channelId);

    // then
    verify(channelRepository).findById(channelId);
    verify(channelRepository).deleteById(channelId);
  }

  @Test
  void deleteChannel_fail_whenChannelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();

    when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(ChannelNotFoundException.class,
        () -> channelService.deleteChannel(channelId));

    verify(channelRepository).findById(channelId);
    verify(channelRepository, never()).deleteById(any());
  }

  @Test
  void findAllByUserId_returnsPublicAndParticipatedPrivateChannels() {
    // given
    UUID userId = UUID.randomUUID();
    
    UUID privateJoinedChannelId = UUID.randomUUID();
    UUID privateNotJoinedChannelId = UUID.randomUUID();

    Channel publicChannel = mock(Channel.class);
    Channel privateJoinedChannel = mock(Channel.class);
    Channel privateNotJoinedChannel = mock(Channel.class);

    ChannelDto publicDto = mock(ChannelDto.class);
    ChannelDto privateJoinedDto = mock(ChannelDto.class);

    when(publicChannel.getType()).thenReturn(ChannelType.PUBLIC);

    when(privateJoinedChannel.getType()).thenReturn(ChannelType.PRIVATE);
    when(privateJoinedChannel.getId()).thenReturn(privateJoinedChannelId);

    when(privateNotJoinedChannel.getType()).thenReturn(ChannelType.PRIVATE);
    when(privateNotJoinedChannel.getId()).thenReturn(privateNotJoinedChannelId);

    when(channelRepository.findAll())
        .thenReturn(List.of(publicChannel, privateJoinedChannel, privateNotJoinedChannel));

    when(readStatusRepository.existsByUserIdAndChannelId(userId, privateJoinedChannelId))
        .thenReturn(true);
    when(readStatusRepository.existsByUserIdAndChannelId(userId, privateNotJoinedChannelId))
        .thenReturn(false);

    when(channelMapper.toDto(publicChannel)).thenReturn(publicDto);
    when(channelMapper.toDto(privateJoinedChannel)).thenReturn(privateJoinedDto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertEquals(2, result.size());
    assertEquals(List.of(publicDto, privateJoinedDto), result);

    verify(channelRepository).findAll();
    verify(readStatusRepository).existsByUserIdAndChannelId(userId, privateJoinedChannelId);
    verify(readStatusRepository).existsByUserIdAndChannelId(userId, privateNotJoinedChannelId);
    verify(channelMapper).toDto(publicChannel);
    verify(channelMapper).toDto(privateJoinedChannel);
    verify(channelMapper, never()).toDto(privateNotJoinedChannel);
  }
}