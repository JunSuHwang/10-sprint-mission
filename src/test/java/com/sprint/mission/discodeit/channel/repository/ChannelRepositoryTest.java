package com.sprint.mission.discodeit.channel.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.config.JpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("채널 이름이 존재하면 true를 반환한다")
  void existsByName_returnsTrue() {
    // given
    Channel channel = new Channel("공지방", ChannelType.PUBLIC, "공지 채널");
    channelRepository.save(channel);

    // when
    boolean result = channelRepository.existsByName("공지방");

    // then
    assertTrue(result);
  }

  @Test
  @DisplayName("채널 이름이 존재하지 않으면 false를 반환한다")
  void existsByName_returnsFalse() {
    // given
    Channel channel = new Channel("공지방", ChannelType.PUBLIC, "공지 채널");
    channelRepository.save(channel);

    // when
    boolean result = channelRepository.existsByName("잡담방");

    // then
    assertFalse(result);
  }
}