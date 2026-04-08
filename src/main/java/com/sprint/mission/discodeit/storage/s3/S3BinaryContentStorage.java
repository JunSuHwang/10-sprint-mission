package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.config.AwsProperties;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.exception.StorageException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

  private static final String BINARY_CONTENT_PREFIX = "binary-contents/";
  private final AwsProperties props;
  private final S3Client s3Client;
  private final S3Presigner presigener;

  @Override
  public UUID put(UUID id, byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      throw new StorageException(id);
    }

    PutObjectRequest putReq = PutObjectRequest.builder()
        .bucket(props.getS3().getBucket())
        .key(buildKey(id))
        .contentType("application/octet-stream")
        .build();

    try {
      s3Client.putObject(
          putReq,
          RequestBody.fromInputStream(new ByteArrayInputStream(bytes), bytes.length)
      );
      return id;
    } catch (S3Exception e) {
      throw new StorageException(id);
    }
  }

  @Override
  public InputStream get(UUID id) {
    return s3Client.getObject(GetObjectRequest.builder()
        .bucket(props.getS3().getBucket())
        .key(buildKey(id))
        .build()
    );
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
    String contentType = Optional.ofNullable(binaryContentDto.contentType())
        .orElse("application/octet-stream");

    String url = generatePresignedUrl(binaryContentDto.id().toString(),
        contentType);

    return ResponseEntity.status(302).location(URI.create(url)).build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    String bucket = props.getS3().getBucket();
    String filePath = BINARY_CONTENT_PREFIX + key;

    GetObjectRequest getReq = GetObjectRequest.builder()
        .bucket(bucket)
        .key(filePath)
        .responseContentDisposition("attachment; filename=" + key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest preReq = GetObjectPresignRequest.builder()
        .getObjectRequest(getReq)
        .signatureDuration(Duration.ofSeconds(props.getS3().getPresignedUrlExpiration()))
        .build();

    return presigener.presignGetObject(preReq).url().toString();
  }

  private String buildKey(UUID id) {
    return BINARY_CONTENT_PREFIX + id;
  }
}
