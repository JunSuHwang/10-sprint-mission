package com.sprint.mission.discodeit.binarycontent.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentsRequest;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage storage;

  @Operation(summary = "첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "첨부 파일 조회 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(implementation = BinaryContentDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "404", description = "첨부 파일을 찾을 수 없음",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              examples = @ExampleObject("BinaryContent with id {binaryContentId} not found")
          )
      )
  })
  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @Parameter(description = "조회할 첨부 파일 ID") @PathVariable UUID binaryContentId
  ) {
    return ResponseEntity.ok(binaryContentService.findBinaryContent(binaryContentId));
  }

  @Operation(summary = "여러 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "첨부 파일 목록 조회 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = BinaryContentDto.class))
          )
      )
  })
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @Parameter(description = "조회할 첨부 파일 ID 목록",
          array = @ArraySchema(schema = @Schema(implementation = UUID.class))
      )
      @RequestParam List<UUID> binaryContentIds
  ) {
    BinaryContentsRequest request = new BinaryContentsRequest(binaryContentIds);
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(request));
  }

  @Operation(summary = "파일 다운로드")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "파일 다운로드 성공",
          content = @Content(
              mediaType = MediaType.ALL_VALUE,
              schema = @Schema(type = "string", format = "binary")
          )
      )
  })
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @Parameter(description = "다운로드할 파일 ID", required = true) @PathVariable UUID binaryContentId
  ) {
    BinaryContentDto binaryContent = binaryContentService.findBinaryContent(binaryContentId);
    return storage.download(binaryContent);
  }
}
