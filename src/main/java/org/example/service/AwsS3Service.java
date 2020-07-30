package org.example.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
     void uploadFile(final MultipartFile multipartFile);
}
