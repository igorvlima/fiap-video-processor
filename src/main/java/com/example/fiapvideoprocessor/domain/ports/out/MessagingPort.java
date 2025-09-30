package com.example.fiapvideoprocessor.domain.ports.out;

import com.example.fiapvideoprocessor.shared.dto.VideoDataMessage;
import com.example.fiapvideoprocessor.shared.dto.VideoStatusMessage;

public interface MessagingPort {
    void sendVideoStatus(VideoStatusMessage message);
    void sendVideoData(VideoDataMessage message);
}