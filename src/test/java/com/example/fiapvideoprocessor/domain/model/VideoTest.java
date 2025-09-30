package com.example.fiapvideoprocessor.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VideoTest {

    @Test
    void testVideoCreation() {
        // Arrange
        String id = "123";
        String name = "test-video.mp4";
        String customerId = "customer123";
        String customerEmail = "customer@example.com";
        String s3Key = "videos/123.mp4";

        // Act
        Video video = new Video(id, name, customerId, customerEmail, s3Key);

        // Assert
        assertNotNull(video);
        assertEquals(id, video.getId());
        assertEquals(name, video.getName());
        assertEquals(customerId, video.getCustomerId());
        assertEquals(customerEmail, video.getCustomerEmail());
        assertEquals(s3Key, video.getS3Key());
    }

    @Test
    void testVideoGetters() {
        // Arrange
        String id = "456";
        String name = "another-video.mp4";
        String customerId = "customer456";
        String customerEmail = "another@example.com";
        String s3Key = "videos/456.mp4";
        Video video = new Video(id, name, customerId, customerEmail, s3Key);

        // Act & Assert
        assertEquals(id, video.getId());
        assertEquals(name, video.getName());
        assertEquals(customerId, video.getCustomerId());
        assertEquals(customerEmail, video.getCustomerEmail());
        assertEquals(s3Key, video.getS3Key());
    }
}