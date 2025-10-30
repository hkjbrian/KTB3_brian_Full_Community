package com.community.domain.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(name = "File", description = "파일 관리 API")
public interface FileApiSpec {

    @Operation(summary = "파일 다운로드", description = "fileId 를 기준으로 해당 파일을 다운로드합니다.")
    ResponseEntity<Resource> download(String fileId);

    @Operation(summary = "파일 삭제", description = "fileId 를 기준으로 해당 파일을 삭제합니다.")
    ResponseEntity<Void> delete(String fileId);
}
