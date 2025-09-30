package com.example.fiapvideoprocessor.util;

import com.example.fiapvideoprocessor.domain.model.Video;
import com.example.fiapvideoprocessor.shared.dto.ProcessingResult;
import com.example.fiapvideoprocessor.shared.dto.VideoDataMessage;
import com.example.fiapvideoprocessor.shared.dto.VideoProcessorMessage;
import com.example.fiapvideoprocessor.shared.dto.VideoStatusMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * Utility class for tests
 */
public class TestUtils {

    /**
     * Creates a test video file
     * @return Path to the created file
     */
    public static Path createTestVideoFile() throws IOException {
        Path tempFile = Files.createTempFile("test-video", ".mp4");
        // Create a small dummy file
        Files.write(tempFile, new byte[1024]);
        return tempFile;
    }

    /**
     * Creates a test video object
     * @return Video object for testing
     */
    public static Video createTestVideo() {
        String videoId = UUID.randomUUID().toString();
        return new Video(
                videoId,
                "test-video.mp4",
                "customer123",
                "customer@example.com",
                "videos/" + videoId + ".mp4"
        );
    }

    /**
     * Creates a test VideoProcessorMessage
     * @return VideoProcessorMessage for testing
     */
    public static VideoProcessorMessage createTestVideoProcessorMessage() {
        String videoId = UUID.randomUUID().toString();
        return new VideoProcessorMessage(
                videoId,
                "test-video.mp4",
                "customer123",
                "customer@example.com",
                "videos/" + videoId + ".mp4"
        );
    }

    /**
     * Creates a test VideoStatusMessage
     * @return VideoStatusMessage for testing
     */
    public static VideoStatusMessage createTestVideoStatusMessage() {
        return new VideoStatusMessage(
                UUID.randomUUID(),
                "test-video.mp4",
                "customer@example.com",
                "COMPLETED"
        );
    }

    /**
     * Creates a test VideoDataMessage
     * @return VideoDataMessage for testing
     */
    public static VideoDataMessage createTestVideoDataMessage() {
        String videoId = UUID.randomUUID().toString();
        return new VideoDataMessage(
                videoId,
                "test-video.mp4",
                "customer123",
                "customer@example.com",
                "processed-videos/frames_" + videoId + ".zip"
        );
    }

    /**
     * Creates a test ProcessingResult
     * @return ProcessingResult for testing
     */
    public static ProcessingResult createTestProcessingResult() {
        return new ProcessingResult(
                true,
                "Processing completed! 10 extracted frames.",
                "frames_test.zip",
                10,
                List.of("frame_0001.png", "frame_0002.png")
        );
    }

    /**
     * Creates test directories for temporary files and outputs
     */
    public static void createTestDirectories() {
        new File("test-temp").mkdirs();
        new File("test-outputs").mkdirs();
    }

    /**
     * Cleans up test directories
     */
    public static void cleanupTestDirectories() {
        deleteDirectory(new File("test-temp"));
        deleteDirectory(new File("test-outputs"));
    }

    /**
     * Recursively deletes a directory
     */
    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}