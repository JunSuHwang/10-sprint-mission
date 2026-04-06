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
        throw new StorageException(null);
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path path = resolvePath(id);
    try {
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new StorageException(id);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return Files.newInputStream(path);
    } catch (IOException e) {
      throw new StorageException(id);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    log.debug("[BINARY_CONTENT_DOWNLOAD] 파일 다운로드 시작 id={}", binaryContentDto.id());
    Path path = resolvePath(binaryContentDto.id());
    try {
      Resource resource = new InputStreamResource(Files.newInputStream(path));
      log.info("[BINARY_CONTENT_DOWNLOAD] 파일 다운로드 id={}", binaryContentDto.id());
      return ResponseEntity
          .status(200)
          .contentType(MediaType.valueOf(binaryContentDto.contentType()))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .body(resource);
    } catch (IOException e) {
      throw new StorageException(binaryContentDto.id());
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
