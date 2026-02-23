package com.sprint.mission.discodeit.readstatus.controller;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
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
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @RequestBody ReadStatusCreateRequest statusInfo) {
    return ResponseEntity.ok(readStatusService.createReadStatus(statusInfo));
  }

  @RequestMapping(value = "/{statusId}", method = RequestMethod.PATCH)
  public ResponseEntity<Void> updateReadStatus(@PathVariable UUID statusId) {
    readStatusService.updateReadStatus(statusId);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(method = RequestMethod.PATCH)
  public ResponseEntity<Void> updateReadStatus(
      @RequestParam UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    readStatusService.updateReadStatus(readStatusId, request);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusDto>> getReadStatuses(@PathVariable UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusDto>> getAllReadStatuses() {
    return ResponseEntity.ok(readStatusService.findAll());
  }

  @RequestMapping(value = "/{statusId}", method = RequestMethod.GET)
  public ResponseEntity<ReadStatusDto> getReadStatus(@PathVariable UUID statusId) {
    return ResponseEntity.ok(readStatusService.find(statusId));
  }
}
