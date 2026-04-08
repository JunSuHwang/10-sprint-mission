package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.config.AwsProperties;
import com.sprint.mission.discodeit.config.AwsProperties.S3;
import com.sprint.mission.discodeit.storage.exception.StorageException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner presigner;

  @Mock
  private AwsProperties props;

  @Mock
  private AwsProperties.S3 s3;

  @InjectMocks
  private S3BinaryContentStorage storage;

  @BeforeEach
  void setUp() {
    when(props.getS3()).thenReturn(s3);
    when(props.getS3().getBucket()).thenReturn("test-bucket");
  }

  @Test
  @DisplayName("put - S3에 바이트 배열을 업로드하고 id를 반환한다")
  void put_success() {
    // given
    UUID id = UUID.randomUUID();
    byte[] bytes = "hello".getBytes();

    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn(PutObjectResponse.builder().build());

    // when
    UUID result = storage.put(id, bytes);

    // then
    assertEquals(id, result);

    verify(s3Client).putObject(
        argThat((PutObjectRequest req) ->
            req.bucket().equals("test-bucket")
                && req.key().equals("binary-contents/" + id)
                && req.contentType().equals("application/octet-stream")
        ),
        any(RequestBody.class)
    );
  }

  @Test
  @DisplayName("put - S3Exception 발생 시 StorageException으로 변환한다")
  void put_fail_throwStorageException() {
    // given
    UUID id = UUID.randomUUID();
    byte[] bytes = "hello".getBytes();

    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenThrow(S3Exception.builder().message("S3 put failed").build());

    // when & then
    assertThrows(StorageException.class, () -> storage.put(id, bytes));
  }

  @Test
  @DisplayName("get - S3에서 InputStream을 조회한다")
  void get_success() {
    // given
    UUID id = UUID.randomUUID();

    ResponseInputStream<GetObjectResponse> responseInputStream =
        new ResponseInputStream<>(
            GetObjectResponse.builder().build(),
            AbortableInputStream.create(new ByteArrayInputStream("data".getBytes()))
        );

    when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(responseInputStream);

    // when
    InputStream result = storage.get(id);

    // then
    assertSame(responseInputStream, result);

    verify(s3Client).getObject(
        argThat((GetObjectRequest req) ->
            req.bucket().equals("test-bucket")
                && req.key().equals("binary-contents/" + id)
        )
    );
  }

  @Test
  @DisplayName("download - presigned URL로 302 리다이렉트 응답을 반환한다")
  void download_success() throws MalformedURLException {
    // given
    UUID id = UUID.randomUUID();
    BinaryContentDto dto = new BinaryContentDto(
        id,
        "profile.png",
        123L,
        "image/png"
    );

    PresignedGetObjectRequest presignedRequest = org.mockito.Mockito.mock(
        PresignedGetObjectRequest.class);
    when(presignedRequest.url()).thenReturn(new URL("https://example.com/download"));

    when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .thenReturn(presignedRequest);

    // when
    ResponseEntity<Void> response = storage.download(dto);

    // then
    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertNotNull(response.getHeaders().getLocation());
    assertEquals("https://example.com/download", response.getHeaders().getLocation().toString());
  }

  @Test
  @DisplayName("download - presigner 호출 시 올바른 bucket, key, contentType을 사용한다")
  void download_presignerRequestValues() throws MalformedURLException {
    // given
    UUID id = UUID.randomUUID();
    BinaryContentDto dto = new BinaryContentDto(
        id,
        "profile.png",
        123L,
        "image/png"
    );

    PresignedGetObjectRequest presignedRequest = org.mockito.Mockito.mock(
        PresignedGetObjectRequest.class);
    when(presignedRequest.url()).thenReturn(new URL("https://example.com/download"));

    ArgumentCaptor<GetObjectPresignRequest> captor =
        ArgumentCaptor.forClass(GetObjectPresignRequest.class);

    when(presigner.presignGetObject(captor.capture()))
        .thenReturn(presignedRequest);

    // when
    storage.download(dto);

    // then
    GetObjectPresignRequest actual = captor.getValue();
    GetObjectRequest getObjectRequest = actual.getObjectRequest();

    assertEquals("test-bucket", getObjectRequest.bucket());
    assertEquals("binary-contents/" + id, getObjectRequest.key());
    assertEquals("image/png", getObjectRequest.responseContentType());
    assertTrue(getObjectRequest.responseContentDisposition().contains(id.toString()));
  }
}