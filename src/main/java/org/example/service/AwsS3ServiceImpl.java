package org.example.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {
    private static final Logger log = LoggerFactory.getLogger(AwsS3ServiceImpl.class);
    @Autowired
    private AmazonS3 amazonS3;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public void uploadFile(MultipartFile multipartFile) {
        log.info("File uploading in progress");
        try {
            File file = convertMultiPartFiletoFile(multipartFile);
            uploadFileToS3Bucket(bucketName, file);
            log.info("file is uploaded successfully");
            file.delete();//to delete the file locally created in the project folder
        } catch (final AmazonServiceException ex) {
            log.info("file uploading is filed ");
            log.error("reason of failure ->" + ex.getMessage());

        }

    }

    //upload file to s3 bucket
    private void uploadFileToS3Bucket(String bucketName, File file) {
        final String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
        log.info("Uploading file with name =" + uniqueFileName);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        amazonS3.putObject(putObjectRequest);

    }

    //converting multipartfile to file
    private File convertMultiPartFiletoFile(MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
            log.error("file not found exception");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
