package com.example.fiapvideoprocessor.domain.ports.out;

import java.nio.file.Path;

public interface StoragePort {
    void upload(Path filePath, String s3Key);
    Path download(String key);
}