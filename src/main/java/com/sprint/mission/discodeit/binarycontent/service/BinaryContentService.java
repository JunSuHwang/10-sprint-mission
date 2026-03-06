package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentsRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BinaryContentService {

  private final BinaryContentRepository contentRepository;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  public BinaryContentDto createBinaryContent(BinaryContentCreateRequest request) {
    byte[] bytes = request.bytes();
    BinaryContent content = new BinaryContent(request.fileName(), (long) bytes.length,
        request.contentType(),
        bytes);
    contentRepository.save(content);
    return binaryContentMapper.toDto(content);
  }

  public BinaryContentDto findBinaryContent(UUID contentId) {
    BinaryContent content = contentRepository.findById(contentId)
        .orElseThrow(BinaryContentNotFoundException::new);
    return binaryContentMapper.toDto(content);
  }

  public BinaryContent findBinaryContentEntity(UUID contentId) {
    return contentRepository.findById(contentId)
        .orElseThrow(BinaryContentNotFoundException::new);
  }

  public List<BinaryContentDto> findAll() {
    return contentRepository.findAll()
        .stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  public List<BinaryContentDto> findAllByIdIn(BinaryContentsRequest request) {
    return contentRepository.findAll()
        .stream()
        .filter(content -> request.ids().contains(content.getId()))
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Transactional
  public void deleteBinaryContent(UUID contentId) {
    contentRepository.deleteById(contentId);
  }
}
