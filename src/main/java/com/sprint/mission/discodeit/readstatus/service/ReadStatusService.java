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

@RequiredArgsConstructor
@Service
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public ReadStatusDto createReadStatus(ReadStatusCreateRequest statusInfo) {
    User user = userRepository.findById(statusInfo.userId())
        .orElseThrow(UserNotFoundException::new);
    Channel channel = channelRepository.findById(statusInfo.channelId())
        .orElseThrow(ChannelNotFoundException::new);

    if (readStatusRepository.findByUserIdAndChannelId(user.getId(), channel.getId())
        .isPresent()) {
      throw new ReadStatusDuplicationException();
    }

    ReadStatus readStatus = new ReadStatus(channel.getId(), user.getId());
    readStatusRepository.save(readStatus);
    return ReadStatusMapper.toReadStatusDto(readStatus);
  }

  public ReadStatusDto find(UUID statusId) {
    ReadStatus readStatus = readStatusRepository.findById(statusId)
        .orElseThrow(ReadStatusNotFoundException::new);
    return ReadStatusMapper.toReadStatusDto(readStatus);
  }

  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId)
        .stream()
        .map(ReadStatusMapper::toReadStatusDto)
        .toList();
  }

  public List<ReadStatusDto> findAll() {
    return readStatusRepository.findAll()
        .stream()
        .map(ReadStatusMapper::toReadStatusDto)
        .toList();
  }

  public ReadStatusDto updateReadStatus(UUID statusId) {
    ReadStatus readStatus = readStatusRepository.findById(statusId)
        .orElseThrow(ReadStatusNotFoundException::new);
    readStatus.updateLastReadAt();
    readStatusRepository.save(readStatus);
    return ReadStatusMapper.toReadStatusDto(readStatus);
  }

  public ReadStatusDto updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(ReadStatusNotFoundException::new);
    readStatus.update(request.newLastReadAt());
    readStatusRepository.save(readStatus);
    return ReadStatusMapper.toReadStatusDto(readStatus);
  }

  public void deleteReadStatus(UUID statusId) {
    readStatusRepository.deleteById(statusId);
  }
}
