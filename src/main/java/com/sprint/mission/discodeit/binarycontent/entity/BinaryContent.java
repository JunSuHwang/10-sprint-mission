package com.sprint.mission.discodeit.binarycontent.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt;
  private final String fileName;
  private final Long size;
  private final String contentType;
  private final byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return Arrays.copyOf(bytes, bytes.length);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return id.equals(((BinaryContent) obj).id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
