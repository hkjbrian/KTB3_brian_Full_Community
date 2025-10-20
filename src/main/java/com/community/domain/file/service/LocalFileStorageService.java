package com.community.domain.file.service;

import com.community.domain.file.service.dto.StoredFile;
import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${host}")
    private String HOST;

    private static final String FILE_ENDPOINT_PREFIX = "/files/";
    private final Map<String, StoredFile> store = new ConcurrentHashMap<>();

    @Override
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_FILE);
        }

        try {
            String fileId = UUID.randomUUID().toString();
            String filePath = HOST + FILE_ENDPOINT_PREFIX + fileId;

            StoredFile storedFile = new StoredFile(
                    filePath,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    file.getBytes()
            );

            store.put(fileId, storedFile);
            return filePath;
        } catch (IOException exception) {
            throw new CustomException(ErrorCode.FILE_STORAGE_ERROR);
        }
    }

    @Override
    public StoredFile load(String fileId) {
        StoredFile storedFile = store.get(fileId);
        if (storedFile == null) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
        return storedFile;
    }

    @Override
    public void delete(String fileId) {
        StoredFile removed = store.remove(fileId);
        if (removed == null) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }
}
