package com.example.fiapvideoprocessor.shared.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VideoDataMessageTest {

    @Test
    void testVideoDataMessageCreation() {
        // Arrange
        String videoId = "123";
        String videoName = "test-video.mp4";
        String customerId = "customer123";
        String customerEmail = "customer@example.com";
        String s3Key = "processed-videos/frames_123.zip";

        // Act
        VideoDataMessage message = new VideoDataMessage(videoId, videoName, customerId, customerEmail, s3Key);

        // Assert
        assertNotNull(message);
        assertEquals(videoId, message.getVideoId());
        assertEquals(videoName, message.getVideoName());
        assertEquals(customerId, message.getCustomerId());
        assertEquals(customerEmail, message.getCustomerEmail());
        assertEquals(s3Key, message.getS3Key());
    }

    @Test
    void testVideoDataMessageNoArgsConstructor() {
        // Act
        VideoDataMessage message = new VideoDataMessage();

        // Assert
        assertNotNull(message);
    }

    @Test
    void testVideoDataMessageSettersAndGetters() {
        // Arrange
        String videoId = "456";
        String videoName = "another-video.mp4";
        String customerId = "customer456";
        String customerEmail = "another@example.com";
        String s3Key = "processed-videos/frames_456.zip";
        VideoDataMessage message = new VideoDataMessage();

        // Act
        message.setVideoId(videoId);
        message.setVideoName(videoName);
        message.setCustomerId(customerId);
        message.setCustomerEmail(customerEmail);
        message.setS3Key(s3Key);

        // Assert
        assertEquals(videoId, message.getVideoId());
        assertEquals(videoName, message.getVideoName());
        assertEquals(customerId, message.getCustomerId());
        assertEquals(customerEmail, message.getCustomerEmail());
        assertEquals(s3Key, message.getS3Key());
    }

    @Test
    void testVideoDataMessageEqualsAndHashCode() {
        // Arrange
        String videoId = "789";
        String videoName = "test-video.mp4";
        String customerId = "customer789";
        String customerEmail = "test@example.com";
        String s3Key = "processed-videos/frames_789.zip";
        
        VideoDataMessage message1 = new VideoDataMessage(videoId, videoName, customerId, customerEmail, s3Key);
        VideoDataMessage message2 = new VideoDataMessage(videoId, videoName, customerId, customerEmail, s3Key);

        // Act & Assert
        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    void testVideoDataMessageToString() {
        // Arrange
        String videoId = "123";
        String videoName = "test-video.mp4";
        String customerId = "customer123";
        String customerEmail = "customer@example.com";
        String s3Key = "processed-videos/frames_123.zip";
        VideoDataMessage message = new VideoDataMessage(videoId, videoName, customerId, customerEmail, s3Key);

        // Act
        String toString = message.toString();

        // Assert
        assertNotNull(toString);
        // Verify that toString contains all field values
        assert(toString.contains(videoId));
        assert(toString.contains(videoName));
        assert(toString.contains(customerId));
        assert(toString.contains(customerEmail));
        assert(toString.contains(s3Key));
    }
}