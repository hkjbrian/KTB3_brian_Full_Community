package com.community.domain.file.service.dto;

import lombok.Data;

@Data
public class StoredFile {

    private final String filePath;
    private final String originalFilename;
    private final String contentType;
    private final long size;
    private final byte[] content;

    public StoredFile(String filePath, String originalFilename, String contentType, long size, byte[] content) {
        this.filePath = filePath;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
    }
}
