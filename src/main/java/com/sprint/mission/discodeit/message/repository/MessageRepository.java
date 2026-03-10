package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByAuthorId(UUID authorId);

  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  Optional<Message> findFirstByChannel_IdOrderByCreatedAtDesc(UUID channelId);
}
