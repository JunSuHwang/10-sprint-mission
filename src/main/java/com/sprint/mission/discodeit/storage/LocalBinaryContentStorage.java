package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
      } catch (Exception e) {
        throw new RuntimeException("Failed to create root directory", e);
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path path = resolvePath(id);
    try {
      Files.write(path, bytes);
    } catch (Exception e) {
      throw new RuntimeException("Failed to write file", e);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return Files.newInputStream(path);
    } catch (Exception e) {
      throw new RuntimeException("Failed to read file", e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    Path path = resolvePath(binaryContentDto.id());
    try {
      byte[] bytes = Files.readAllBytes(path);
      Resource resource = new ByteArrayResource(bytes);
      return ResponseEntity
          .status(200)
          .contentType(MediaType.valueOf(binaryContentDto.contentType()))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .body(resource);
    } catch (Exception e) {
      throw new RuntimeException("Failed to read file", e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
