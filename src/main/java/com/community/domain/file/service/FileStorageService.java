package com.community.domain.file.service;

import com.community.domain.file.service.dto.StoredFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String save(MultipartFile file);

    StoredFile load(String filePath);

    void delete(String filePath);
}
