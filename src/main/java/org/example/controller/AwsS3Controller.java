package org.example.controller;

import org.example.service.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3")
public class AwsS3Controller {
    @Autowired
    AwsS3Service awsS3Service;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") final MultipartFile multipartFile) {
        awsS3Service.uploadFile(multipartFile);
        final String response = "[" + multipartFile.getOriginalFilename() + "]" + " Uploaded successfully";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
