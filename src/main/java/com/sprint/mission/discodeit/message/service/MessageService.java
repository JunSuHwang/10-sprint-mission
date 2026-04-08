package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.common.dto.MyPageRequest;
import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto createMessage(MessageCreateRequest createInfo,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  MessageDto findMessage(UUID messageId);

  List<MessageDto> findAll();

  List<MessageDto> findAllByUserId(UUID userId);

  PageResponse<MessageDto> findAllByChannelId(MyPageRequest<UUID> request);

  MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageInfo);

  void deleteMessage(UUID messageId);
}
