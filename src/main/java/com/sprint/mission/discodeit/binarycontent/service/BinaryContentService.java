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

  @Transactional
  public BinaryContentDto createBinaryContent(BinaryContentCreateRequest contentInfo) {
    byte[] bytes = contentInfo.bytes();
    BinaryContent content = new BinaryContent(contentInfo.fileName(), (long) bytes.length,
        contentInfo.contentType(),
        bytes);
    contentRepository.save(content);
    return BinaryContentMapper.toBinaryContentDto(content);
  }

  public BinaryContentDto findBinaryContent(UUID contentId) {
    BinaryContent content = contentRepository.findById(contentId)
        .orElseThrow(BinaryContentNotFoundException::new);
    return BinaryContentMapper.toBinaryContentDto(content);
  }

  public BinaryContent findBinaryContentEntity(UUID contentId) {
    return contentRepository.findById(contentId)
        .orElseThrow(BinaryContentNotFoundException::new);
  }

  public List<BinaryContentDto> findAll() {
    return contentRepository.findAll()
        .stream()
        .map(BinaryContentMapper::toBinaryContentDto)
        .toList();
  }
  
  public List<BinaryContentDto> findAllByIdIn(BinaryContentsRequest request) {
    return contentRepository.findAll()
        .stream()
        .filter(content -> request.ids().contains(content.getId()))
        .map(BinaryContentMapper::toBinaryContentDto)
        .toList();
  }

  @Transactional
  public void deleteBinaryContent(UUID contentId) {
    contentRepository.deleteById(contentId);
  }
}
