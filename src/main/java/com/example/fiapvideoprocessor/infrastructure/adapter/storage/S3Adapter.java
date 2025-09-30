package com.example.fiapvideoprocessor.infrastructure.adapter.storage;

import com.example.fiapvideoprocessor.domain.ports.out.StoragePort;
import com.example.fiapvideoprocessor.infrastructure.config.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
@RequiredArgsConstructor
public class S3Adapter implements StoragePort {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    @Override
    public void upload(Path filePath, String s3Key) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build();

            s3Client.putObject(putObjectRequest, filePath);
        } catch (S3Exception e) {
            throw new RuntimeException("Error to send file to S3: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    @Override
    public Path download(String s3Key) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);

            Path tempFile = Files.createTempFile("video_", "_" + s3Key.replace("/", "_"));
            Files.write(tempFile, objectBytes.asByteArray(), StandardOpenOption.WRITE);

            return tempFile;
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("Object not found on S3 with key: " + s3Key, e);
        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Error to download file from S3", e);
        }
    }
}