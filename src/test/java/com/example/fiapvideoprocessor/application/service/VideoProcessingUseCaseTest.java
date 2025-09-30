package com.example.fiapvideoprocessor.application.service;

import com.example.fiapvideoprocessor.domain.model.Video;
import com.example.fiapvideoprocessor.domain.ports.out.MessagingPort;
import com.example.fiapvideoprocessor.domain.ports.out.StoragePort;
import com.example.fiapvideoprocessor.shared.dto.VideoDataMessage;
import com.example.fiapvideoprocessor.shared.dto.VideoStatusMessage;
import com.example.fiapvideoprocessor.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoProcessingUseCaseTest {

    @Mock
    private StoragePort storagePort;

    @Mock
    private MessagingPort messagingPort;

    @Captor
    private ArgumentCaptor<VideoStatusMessage> statusMessageCaptor;

    @Captor
    private ArgumentCaptor<VideoDataMessage> dataMessageCaptor;

    private VideoProcessingUseCase videoProcessingUseCase;

    @BeforeEach
    void setUp() {
        videoProcessingUseCase = new VideoProcessingUseCase(storagePort, messagingPort);
        ReflectionTestUtils.setField(videoProcessingUseCase, "bucketUrl", "http://test-bucket.s3.amazonaws.com/");

        // Create test directories
        TestUtils.createTestDirectories();

        // Create outputs directory for the test
        new File("outputs").mkdirs();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test directories
        TestUtils.cleanupTestDirectories();

        // Clean up outputs directory
        Path outputsDir = Path.of("outputs");
        if (Files.exists(outputsDir)) {
            Files.walk(outputsDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testProcessVideoSuccess() throws IOException {
        // Arrange
        Video video = TestUtils.createTestVideo();
        Path testVideoFile = TestUtils.createTestVideoFile();
        assertTrue(Files.exists(testVideoFile));

        // Mock the storage port to return our test file
        when(storagePort.download(eq(video.getS3Key()))).thenReturn(testVideoFile);

        doNothing().when(storagePort).upload(any(), any());

        // Act
        videoProcessingUseCase.process(video);

        // Assert
        // Verify that the storage port was called to download the video
        verify(storagePort).download(eq(video.getS3Key()));

        // Verify that the messaging port was called to send status and data messages
        verify(messagingPort).sendVideoStatus(statusMessageCaptor.capture());
        verify(messagingPort).sendVideoData(dataMessageCaptor.capture());

        // Verify the content of the status message
        VideoStatusMessage statusMessage = statusMessageCaptor.getValue();
        assertEquals(UUID.fromString(video.getId()), statusMessage.getVideoId());
        assertEquals(video.getName(), statusMessage.getVideoName());
        assertEquals(video.getCustomerEmail(), statusMessage.getCustomerEmail());
        assertEquals("COMPLETED", statusMessage.getVideoStatus());

        // Verify the content of the data message
        VideoDataMessage dataMessage = dataMessageCaptor.getValue();
        assertEquals(video.getId(), dataMessage.getVideoId());
        assertEquals(video.getName(), dataMessage.getVideoName());
        assertEquals(video.getCustomerId(), dataMessage.getCustomerId());
        assertEquals(video.getCustomerEmail(), dataMessage.getCustomerEmail());
        assertTrue(dataMessage.getS3Key().contains("processed-videos/"));
    }

    @Test
    void testProcessVideoWithDownloadError() {
        // Arrange
        Video video = TestUtils.createTestVideo();
        
        // Mock the storage port to throw an exception
        when(storagePort.download(any())).thenThrow(new RuntimeException("Download error"));
        
        // Act
        videoProcessingUseCase.process(video);
        
        // Assert
        // Verify that the storage port was called to download the video
        verify(storagePort).download(eq(video.getS3Key()));
        
        // Verify that the messaging port was called to send an error status message
        verify(messagingPort).sendVideoStatus(statusMessageCaptor.capture());
        
        // Verify that the data message was not sent
        verify(messagingPort, never()).sendVideoData(any());
        
        // Verify the content of the status message
        VideoStatusMessage statusMessage = statusMessageCaptor.getValue();
        assertEquals(UUID.fromString(video.getId()), statusMessage.getVideoId());
        assertEquals(video.getName(), statusMessage.getVideoName());
        assertEquals("ERROR", statusMessage.getVideoStatus());
        assertEquals(video.getCustomerEmail(), statusMessage.getCustomerEmail());
    }

    @Test
    void testProcessVideoWithUploadError() throws IOException {
        // Arrange
        Video video = TestUtils.createTestVideo();
        Path testVideoFile = TestUtils.createTestVideoFile();
        
        // Mock the storage port to return our test file for download
        when(storagePort.download(eq(video.getS3Key()))).thenReturn(testVideoFile);
        
        // Mock the storage port to throw an exception on upload
        doThrow(new RuntimeException("Upload error")).when(storagePort).upload(any(), any());
        
        // Act
        videoProcessingUseCase.process(video);
        
        // Assert
        // Verify that the storage port was called to download the video
        verify(storagePort).download(eq(video.getS3Key()));
        
        // Verify that the storage port was called to upload the processed video
        verify(storagePort).upload(any(), any());
        
        // Verify that the messaging port was called to send an error status message
        verify(messagingPort).sendVideoStatus(statusMessageCaptor.capture());
        
        // Verify that the data message was not sent
        verify(messagingPort, never()).sendVideoData(any());
        
        // Verify the content of the status message
        VideoStatusMessage statusMessage = statusMessageCaptor.getValue();
        assertEquals(UUID.fromString(video.getId()), statusMessage.getVideoId());
        assertEquals(video.getName(), statusMessage.getVideoName());
        assertEquals("ERROR", statusMessage.getVideoStatus());
        assertEquals(video.getCustomerEmail(), statusMessage.getCustomerEmail());
    }
}