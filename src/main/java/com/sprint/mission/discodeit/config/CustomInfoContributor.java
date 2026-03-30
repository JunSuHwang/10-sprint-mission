package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class CustomInfoContributor implements InfoContributor {

  @Override
  public void contribute(Builder builder) {
    builder.withDetail("spring", Map.of("bootVersion", SpringBootVersion.getVersion()));
  }
}
