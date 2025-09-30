package com.example.fiapvideoprocessor.shared.dto;

import java.util.List;

public record ProcessingResult(
        boolean success,
        String message,
        String outputZip,
        int frameCount,
        List<String> frameNames
) {}