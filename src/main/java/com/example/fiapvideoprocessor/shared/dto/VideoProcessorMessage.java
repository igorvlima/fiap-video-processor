package com.example.fiapvideoprocessor.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoProcessorMessage {
    private String videoId;
    private String videoName;
    private String customerId;
    private String customerEmail;
    private String s3Key;
}