package com.sprint.mission.discodeit.message.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("작성자 ID로 메시지 목록을 조회할 수 있다")
  void findAllByAuthorId_success() {
    // given
    User author = userRepository.save(new User("user1", "qwer123", "user1@gmail.com"));
    User otherUser = userRepository.save(new User("user2", "qwer123", "user2@gmail.com"));

    Channel channel = channelRepository.save(new Channel("공지방", ChannelType.PUBLIC, "공지"));

    Message message1 = new Message("작성자 메시지1", channel, author, List.of());
    Message message2 = new Message("작성자 메시지2", channel, author, List.of());
    Message otherMessage = new Message("다른 사용자 메시지", channel, otherUser, List.of());

    messageRepository.save(message1);
    messageRepository.save(message2);
    messageRepository.save(otherMessage);

    // when
    List<Message> result = messageRepository.findAllByAuthorId(author.getId());

    // then
    assertEquals(2, result.size());
    assertTrue(
        result.stream().allMatch(message -> message.getAuthor().getId().equals(author.getId())));
  }

  @Test
  @DisplayName("채널 ID로 첫 페이지 메시지를 최신순으로 조회한다")
  void findFirstPageByChannelId_success() {
    // given
    User author = userRepository.save(new User("user1", "qwer123", "user1@gmail.com"));
    Channel channel = channelRepository.save(new Channel("공지방", ChannelType.PUBLIC, "공지"));

    Message oldMessage = new Message("old", channel, author, List.of());
    Message middleMessage = new Message("middle", channel, author, List.of());
    Message newMessage = new Message("new", channel, author, List.of());

    messageRepository.save(oldMessage);
    messageRepository.save(middleMessage);
    messageRepository.save(newMessage);

    ReflectionTestUtils.setField(oldMessage, "createdAt", Instant.parse("2025-01-01T10:00:00Z"));
    ReflectionTestUtils.setField(middleMessage, "createdAt", Instant.parse("2025-01-01T11:00:00Z"));
    ReflectionTestUtils.setField(newMessage, "createdAt", Instant.parse("2025-01-01T12:00:00Z"));

    messageRepository.save(oldMessage);
    messageRepository.save(middleMessage);
    messageRepository.save(newMessage);

    Pageable pageable = PageRequest.of(0, 2);

    // when
    List<Message> result = messageRepository.findFirstPageByChannelId(channel.getId(), pageable);

    // then
    assertEquals(2, result.size());
    assertEquals("new", result.get(0).getContent());
    assertEquals("middle", result.get(1).getContent());
  }

  @Test
  @DisplayName("커서 이전의 다음 페이지 메시지를 최신순으로 조회한다")
  void findNextPageByChannelId_success() {
    // given
    User author = userRepository.save(new User("user1", "qwer123", "user1@gmail.com"));
    Channel channel = channelRepository.save(new Channel("공지방", ChannelType.PUBLIC, "공지"));

    Message oldest = messageRepository.save(new Message("oldest", channel, author, List.of()));
    Message older = messageRepository.save(new Message("older", channel, author, List.of()));
    Message newer = messageRepository.save(new Message("newer", channel, author, List.of()));

    Instant oldestTime = Instant.parse("2025-01-01T10:00:00Z");
    Instant olderTime = Instant.parse("2025-01-01T11:00:00Z");
    Instant newerTime = Instant.parse("2025-01-01T12:00:00Z");

    em.getEntityManager()
        .createNativeQuery("update messages set created_at = ? where id = ?")
        .setParameter(1, oldestTime)
        .setParameter(2, oldest.getId())
        .executeUpdate();

    em.getEntityManager()
        .createNativeQuery("update messages set created_at = ? where id = ?")
        .setParameter(1, olderTime)
        .setParameter(2, older.getId())
        .executeUpdate();

    em.getEntityManager()
        .createNativeQuery("update messages set created_at = ? where id = ?")
        .setParameter(1, newerTime)
        .setParameter(2, newer.getId())
        .executeUpdate();

    em.flush();
    em.clear();

    Pageable pageable = PageRequest.of(0, 10);

    // when
    List<Message> result =
        messageRepository.findNextPageByChannelId(channel.getId(), newerTime, pageable);

    // then
    assertEquals(2, result.size());
    assertEquals("older", result.get(0).getContent());
    assertEquals("oldest", result.get(1).getContent());
  }

  @Test
  @DisplayName("채널의 가장 최신 메시지 1개를 조회한다")
  void findFirstByChannel_IdOrderByCreatedAtDesc_success() {
    // given
    User author = userRepository.save(new User("user1", "qwer123", "user1@gmail.com"));
    Channel channel = channelRepository.save(new Channel("공지방", ChannelType.PUBLIC, "공지"));

    Message oldMessage = new Message("old", channel, author, List.of());
    Message newMessage = new Message("new", channel, author, List.of());

    messageRepository.save(oldMessage);
    messageRepository.save(newMessage);

    ReflectionTestUtils.setField(oldMessage, "createdAt", Instant.parse("2025-01-01T10:00:00Z"));
    ReflectionTestUtils.setField(newMessage, "createdAt", Instant.parse("2025-01-01T12:00:00Z"));

    messageRepository.save(oldMessage);
    messageRepository.save(newMessage);

    // when
    Optional<Message> result =
        messageRepository.findFirstByChannel_IdOrderByCreatedAtDesc(channel.getId());

    // then
    assertTrue(result.isPresent());
    assertEquals("new", result.get().getContent());
  }
}