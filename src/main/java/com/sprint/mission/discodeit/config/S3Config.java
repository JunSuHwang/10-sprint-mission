package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  private final AwsProperties props;

  public S3Config(AwsProperties props) {
    this.props = props;
  }

  @Bean
  public S3Client s3Client() {
    if (props.getCredentials().getAccessKey() != null
        && !props.getCredentials().getAccessKey().isBlank()) {
      return S3Client.builder()
          .region(Region.of(props.getRegion()))
          .credentialsProvider(
              StaticCredentialsProvider.create(
                  AwsBasicCredentials.create(
                      props.getCredentials().getAccessKey(),
                      props.getCredentials().getSecretKey()
                  )
              )
          )
          .build();
    }
    return S3Client.builder()
        .region(Region.of(props.getRegion()))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }
}
