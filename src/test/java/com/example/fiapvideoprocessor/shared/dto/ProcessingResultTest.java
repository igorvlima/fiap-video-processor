package com.example.fiapvideoprocessor.shared.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessingResultTest {

    @Test
    void testProcessingResultCreation() {
        // Arrange
        boolean success = true;
        String message = "Processing completed! 10 extracted frames.";
        String outputZip = "frames_test.zip";
        int frameCount = 10;
        List<String> frameNames = List.of("frame_0001.png", "frame_0002.png");

        // Act
        ProcessingResult result = new ProcessingResult(success, message, outputZip, frameCount, frameNames);

        // Assert
        assertNotNull(result);
        assertEquals(success, result.success());
        assertEquals(message, result.message());
        assertEquals(outputZip, result.outputZip());
        assertEquals(frameCount, result.frameCount());
        assertEquals(frameNames, result.frameNames());
    }

    @Test
    void testProcessingResultWithFailure() {
        // Arrange
        boolean success = false;
        String message = "Error in ffmpeg: No frames extracted";
        String outputZip = null;
        int frameCount = 0;
        List<String> frameNames = List.of();

        // Act
        ProcessingResult result = new ProcessingResult(success, message, outputZip, frameCount, frameNames);

        // Assert
        assertNotNull(result);
        assertFalse(result.success());
        assertEquals(message, result.message());
        assertNull(result.outputZip());
        assertEquals(0, result.frameCount());
        assertTrue(result.frameNames().isEmpty());
    }

    @Test
    void testProcessingResultEquality() {
        // Arrange
        boolean success = true;
        String message = "Processing completed! 5 extracted frames.";
        String outputZip = "frames_123.zip";
        int frameCount = 5;
        List<String> frameNames = List.of("frame_0001.png", "frame_0002.png", "frame_0003.png");

        // Act
        ProcessingResult result1 = new ProcessingResult(success, message, outputZip, frameCount, frameNames);
        ProcessingResult result2 = new ProcessingResult(success, message, outputZip, frameCount, frameNames);

        // Assert
        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    void testProcessingResultInequality() {
        // Arrange
        ProcessingResult result1 = new ProcessingResult(true, "Success", "zip1.zip", 10, List.of("frame1.png"));
        ProcessingResult result2 = new ProcessingResult(true, "Success", "zip2.zip", 10, List.of("frame1.png"));
        ProcessingResult result3 = new ProcessingResult(false, "Success", "zip1.zip", 10, List.of("frame1.png"));

        // Assert
        assertNotEquals(result1, result2);
        assertNotEquals(result1, result3);
        assertNotEquals(result2, result3);
    }

    @Test
    void testProcessingResultToString() {
        // Arrange
        boolean success = true;
        String message = "Processing completed! 3 extracted frames.";
        String outputZip = "frames_456.zip";
        int frameCount = 3;
        List<String> frameNames = List.of("frame_0001.png", "frame_0002.png", "frame_0003.png");
        ProcessingResult result = new ProcessingResult(success, message, outputZip, frameCount, frameNames);

        // Act
        String toString = result.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains(String.valueOf(success)));
        assertTrue(toString.contains(message));
        assertTrue(toString.contains(outputZip));
        assertTrue(toString.contains(String.valueOf(frameCount)));
        // Check that the toString contains at least one frame name
        assertTrue(toString.contains("frame_0001.png"));
    }
}