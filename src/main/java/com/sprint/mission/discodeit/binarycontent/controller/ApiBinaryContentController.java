package com.sprint.mission.discodeit.binarycontent.controller;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContent")
public class ApiBinaryContentController {

  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public ResponseEntity<BinaryContent> findBinaryContent(@RequestParam UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContentEntity(binaryContentId));
  }
}
