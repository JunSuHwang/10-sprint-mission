package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByAuthorId(UUID authorId);

  List<Message> findAllByChannelId(UUID channelId);

  Optional<Message> findFirstByChannel_IdOrderByCreatedAtAsc(UUID channelId);
}
