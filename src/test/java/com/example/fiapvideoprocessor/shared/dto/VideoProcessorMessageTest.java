package com.example.fiapvideoprocessor.shared.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VideoProcessorMessageTest {

    @Test
    void testVideoProcessorMessageCreation() {
        // Arrange
        String videoId = "123";
        String videoName = "test-video.mp4";
        String customerId = "customer123";
        String customerEmail = "customer@example.com";
        String s3Key = "videos/123.mp4";

        // Act
        VideoProcessorMessage message = new VideoProcessorMessage(videoId, videoName, customerId, customerEmail, s3Key);

        // Assert
        assertNotNull(message);
        assertEquals(videoId, message.getVideoId());
        assertEquals(videoName, message.getVideoName());
        assertEquals(customerId, message.getCustomerId());
        assertEquals(customerEmail, message.getCustomerEmail());
        assertEquals(s3Key, message.getS3Key());
    }

    @Test
    void testVideoProcessorMessageNoArgsConstructor() {
        // Act
        VideoProcessorMessage message = new VideoProcessorMessage();

        // Assert
        assertNotNull(message);
    }

    @Test
    void testVideoProcessorMessageSettersAndGetters() {
        // Arrange
        String videoId = "456";
        String videoName = "another-video.mp4";
        String customerId = "customer456";
        String customerEmail = "another@example.com";
        String s3Key = "videos/456.mp4";
        VideoProcessorMessage message = new VideoProcessorMessage();

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
    void testVideoProcessorMessageEqualsAndHashCode() {
        // Arrange
        String videoId = "789";
        String videoName = "test-video.mp4";
        String customerId = "customer789";
        String customerEmail = "test@example.com";
        String s3Key = "videos/789.mp4";
        
        VideoProcessorMessage message1 = new VideoProcessorMessage(videoId, videoName, customerId, customerEmail, s3Key);
        VideoProcessorMessage message2 = new VideoProcessorMessage(videoId, videoName, customerId, customerEmail, s3Key);

        // Act & Assert
        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    void testVideoProcessorMessageToString() {
        // Arrange
        String videoId = "123";
        String videoName = "test-video.mp4";
        String customerId = "customer123";
        String customerEmail = "customer@example.com";
        String s3Key = "videos/123.mp4";
        VideoProcessorMessage message = new VideoProcessorMessage(videoId, videoName, customerId, customerEmail, s3Key);

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