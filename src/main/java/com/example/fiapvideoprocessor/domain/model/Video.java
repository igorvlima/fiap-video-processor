package com.example.fiapvideoprocessor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Video {
    private final String id;
    private final String name;
    private final String customerId;
    private final String customerEmail;
    private final String s3Key;
}