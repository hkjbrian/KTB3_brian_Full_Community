package com.community.domain.user.service;

import com.community.global.exception.CustomException;
import com.community.global.exception.ErrorCode;
import com.community.domain.file.service.FileStorageService;
import com.community.domain.user.dto.request.SignInRequest;
import com.community.domain.user.dto.response.SignInResponse;
import com.community.domain.user.model.User;
import com.community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public SignInResponse signIn(SignInRequest req) {
        validateDuplicateUser(req);
        String imageUrl = saveProfileImage(req.getFile());
        Long savedId = userRepository.save(new User(req.getEmail(), req.getPassword(), req.getNickname(), imageUrl));
        return new SignInResponse(savedId);
    }

    private void validateDuplicateUser(SignInRequest req) {
        userRepository.findByEmail(req.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
                });

        userRepository.findByNickName(req.getNickname())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
                });
    }

    private String saveProfileImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        return fileStorageService.save(file);
    }
}
