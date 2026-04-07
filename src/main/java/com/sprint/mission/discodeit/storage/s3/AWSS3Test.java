package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.AwsProperties;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class AWSS3Test {

  private final S3Client s3Client;
  private final S3Presigner presigner;
  private final AwsProperties props;

  @PostMapping("/upload")
  public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) {

    String filePath = "test/" + UUID.randomUUID() + file.getOriginalFilename();

    PutObjectRequest putReq = PutObjectRequest.builder()
        .bucket(props.getS3().getBucket())
        .key(filePath)
        .contentType(file.getContentType())
        .build();

    try {
      s3Client.putObject(putReq,
          RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
      String url = buildPublicUrl(props.getS3().getBucket(), props.getRegion(), filePath);
      return ResponseEntity.created(URI.create(url)).build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/download")
  public ResponseEntity<InputStreamResource> download(@RequestParam String key) {
    ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(GetObjectRequest.builder()
        .bucket(props.getS3().getBucket())
        .key(key)
        .build());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + key + "\"")
        .contentType(MediaType.parseMediaType(s3Object.response().contentType()))
        .body(new InputStreamResource(s3Object));
  }

  @GetMapping("/presigned-download")
  public ResponseEntity<Void> presigedDownload(
      @RequestParam String key,
      @RequestParam(required = false) String filename) {
    String bucket = props.getS3().getBucket();
    String name = (filename != null && !filename.isBlank())
        ? filename
        : Paths.get(key).getFileName().toString();

    GetObjectRequest getReq = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentDisposition("attachment; filename=" + name)
        .build();

    GetObjectPresignRequest preReq = GetObjectPresignRequest.builder()
        .getObjectRequest(getReq)
        .signatureDuration(Duration.ofMinutes(5))
        .build();

    String signed = presigner.presignGetObject(preReq).url().toString();
    return ResponseEntity.status(302).location(URI.create(signed)).build();
  }

  private String buildPublicUrl(String bucket, String region, String key) {
    String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8).replace("+", "%20");
    if (region == null || region.isBlank() || "us-east-1".equals(region)) {
      return "https://" + bucket + ".s3.amazonaws.com/" + encodedKey;
    }
    return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + encodedKey;
  }
}
