package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.storage.exception.StorageException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.local.root-path}") String root) {
    this.root = Path.of(root);
  }

  @PostConstruct
  public void init() {
    if (Files.notExists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        throw new StorageException("디렉터리 생성을 실패했습니다.");
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path path = resolvePath(id);
    try {
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new StorageException("파일 쓰기에 실패했습니다.");
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return Files.newInputStream(path);
    } catch (IOException e) {
      throw new StorageException("파일 읽기에 실패했습니다.");
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    Path path = resolvePath(binaryContentDto.id());
    try {
      Resource resource = new InputStreamResource(Files.newInputStream(path));
      return ResponseEntity
          .status(200)
          .contentType(MediaType.valueOf(binaryContentDto.contentType()))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .body(resource);
    } catch (IOException e) {
      throw new StorageException("다운로드 파일 로드에 실패했습니다.");
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
