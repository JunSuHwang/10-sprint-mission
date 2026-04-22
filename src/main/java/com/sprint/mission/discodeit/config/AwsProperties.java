package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Getter
@Setter
public class AwsProperties {

  private Credentials credentials = new Credentials();
  private String region;
  private S3 s3 = new S3();

  @Getter
  @Setter
  public static class Credentials {

    private String accessKey;
    private String secretKey;
  }

  @Getter
  @Setter
  public static class S3 {

    private String bucket;
    private int presignedUrlExpiration;
  }
}
