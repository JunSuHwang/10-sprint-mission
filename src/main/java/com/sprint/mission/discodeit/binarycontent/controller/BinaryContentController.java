package com.sprint.mission.discodeit.binarycontent.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentInfo;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentsRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "/{contentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentInfo> getBinaryContent(@PathVariable UUID contentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContent(contentId));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContentInfo>> getBinaryContents(
      @RequestBody BinaryContentsRequest request
  ) {
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(request));
  }

  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public ResponseEntity<BinaryContent> findBinaryContent(@RequestParam UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContentEntity(binaryContentId));
  }
}
