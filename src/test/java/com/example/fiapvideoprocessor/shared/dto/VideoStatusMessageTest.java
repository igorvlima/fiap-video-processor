package com.example.fiapvideoprocessor.shared.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VideoStatusMessageTest {

    @Test
    void testVideoStatusMessageCreation() {
        // Arrange
        UUID videoId = UUID.randomUUID();
        String videoName = "test-video.mp4";
        String customerEmail = "customer@example.com";
        String videoStatus = "COMPLETED";

        // Act
        VideoStatusMessage message = new VideoStatusMessage(videoId, videoName, customerEmail, videoStatus);

        // Assert
        assertNotNull(message);
        assertEquals(videoId, message.getVideoId());
        assertEquals(videoName, message.getVideoName());
        assertEquals(customerEmail, message.getCustomerEmail());
        assertEquals(videoStatus, message.getVideoStatus());
    }

    @Test
    void testVideoStatusMessageBuilder() {
        // Arrange
        UUID videoId = UUID.randomUUID();
        String videoName = "test-video.mp4";
        String customerEmail = "customer@example.com";
        String videoStatus = "PROCESSING";

        // Act
        VideoStatusMessage message = VideoStatusMessage.builder()
                .videoId(videoId)
                .videoName(videoName)
                .customerEmail(customerEmail)
                .videoStatus(videoStatus)
                .build();

        // Assert
        assertNotNull(message);
        assertEquals(videoId, message.getVideoId());
        assertEquals(videoName, message.getVideoName());
        assertEquals(customerEmail, message.getCustomerEmail());
        assertEquals(videoStatus, message.getVideoStatus());
    }

    @Test
    void testVideoStatusMessageGetters() {
        // Arrange
        UUID videoId = UUID.randomUUID();
        String videoName = "another-video.mp4";
        String customerEmail = "another@example.com";
        String videoStatus = "ERROR";
        VideoStatusMessage message = new VideoStatusMessage(videoId, videoName, customerEmail, videoStatus);

        // Act & Assert
        assertEquals(videoId, message.getVideoId());
        assertEquals(videoName, message.getVideoName());
        assertEquals(customerEmail, message.getCustomerEmail());
        assertEquals(videoStatus, message.getVideoStatus());
    }
}