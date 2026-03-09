package com.sprint.mission.discodeit.binarycontent.mapper;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentDto toDto(BinaryContent binaryContent);

  @Mapping(target = "size", expression = "java((long) request.bytes().length)")
  BinaryContent toEntity(BinaryContentCreateRequest request);
}