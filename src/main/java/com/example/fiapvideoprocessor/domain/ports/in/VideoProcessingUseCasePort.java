package com.example.fiapvideoprocessor.domain.ports.in;

import com.example.fiapvideoprocessor.domain.model.Video;

public interface VideoProcessingUseCasePort {
    void process(Video video);
}