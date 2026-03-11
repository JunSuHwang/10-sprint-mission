package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByAuthorId(UUID authorId);

  @Query(
      """
              SELECT m 
                        FROM Message m 
                                  WHERE m.channel.id = :channelId  
                                                      ORDER BY m.createdAt DESC
          """)
  List<Message> findFirstPageByChannelId(UUID channelId, Pageable pageable);

  @Query(
      """
              SELECT m 
                        FROM Message m 
                                  WHERE m.channel.id = :channelId 
                                            AND m.createdAt < :cursorCreatedAt
                                                      ORDER BY m.createdAt DESC
          """)
  List<Message> findNextPageByChannelId(UUID channelId, Instant cursorCreatedAt, Pageable pageable);

  Optional<Message> findFirstByChannel_IdOrderByCreatedAtDesc(UUID channelId);
}
