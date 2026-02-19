package com.sprint.mission.discodeit.binarycontent.dto;

public record BinaryContentCreateInfo(
        String fileName,
        String contentType,
        byte[] content
) {}
