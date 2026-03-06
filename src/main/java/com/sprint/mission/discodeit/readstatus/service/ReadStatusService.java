package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.exception.ReadStatusDuplicationException;
import com.sprint.mission.discodeit.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.readstatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  public ReadStatusDto createReadStatus(ReadStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(UserNotFoundException::new);
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(ChannelNotFoundException::new);

    if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
      throw new ReadStatusDuplicationException();
    }

    ReadStatus readStatus = new ReadStatus(user, channel);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(readStatus);
  }

  public ReadStatusDto find(UUID reaStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(reaStatusId)
        .orElseThrow(ReadStatusNotFoundException::new);
    return readStatusMapper.toDto(readStatus);
  }

  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId)
        .stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  public List<ReadStatusDto> findAll() {
    return readStatusRepository.findAll()
        .stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  public ReadStatusDto updateReadStatus(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(ReadStatusNotFoundException::new);
    readStatus.updateLastReadAt();
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  public ReadStatusDto updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(ReadStatusNotFoundException::new);
    readStatus.update(request.newLastReadAt());
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  public void deleteReadStatus(UUID reaStatusId) {
    readStatusRepository.deleteById(reaStatusId);
  }
}
