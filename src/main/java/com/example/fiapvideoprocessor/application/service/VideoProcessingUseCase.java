package com.example.fiapvideoprocessor.application.service;

import com.example.fiapvideoprocessor.domain.model.Video;
import com.example.fiapvideoprocessor.domain.ports.in.VideoProcessingUseCasePort;
import com.example.fiapvideoprocessor.domain.ports.out.MessagingPort;
import com.example.fiapvideoprocessor.domain.ports.out.StoragePort;
import com.example.fiapvideoprocessor.shared.dto.VideoDataMessage;
import com.example.fiapvideoprocessor.shared.dto.VideoStatusMessage;
import com.example.fiapvideoprocessor.shared.dto.ProcessingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoProcessingUseCase implements VideoProcessingUseCasePort {

    private static final String TEMP_DIR = "temp/";
    private static final String OUTPUTS_DIR = "outputs/";
    @Value( "${aws.bucket-url}")
    private String bucketUrl;

    private final StoragePort storagePort;
    private final MessagingPort messagingPort;

    @Override
    public void process(Video video) {
        Path localFile = null;
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String resultZipKey = null;

        try {
            log.info("Downloading video {} from S3", video.getId());
            localFile = storagePort.download(video.getS3Key());

            ProcessingResult result = processVideo(localFile.toFile(), timestamp);

            if (!result.success()) {
                sendErrorMessage(video);
                return;
            }

            File zipFile = new File(OUTPUTS_DIR, result.outputZip());
            resultZipKey = "processed-videos/" + result.outputZip();
            storagePort.upload(zipFile.toPath(), resultZipKey);

            VideoDataMessage dataMessage = new VideoDataMessage(
                    video.getId(),
                    video.getName(),
                    video.getCustomerId(),
                    video.getCustomerEmail(),
                    bucketUrl+resultZipKey
            );

            VideoStatusMessage statusMessage = new VideoStatusMessage(
                    UUID.fromString(video.getId()),
                    video.getName(),
                    video.getCustomerEmail(),
                    "COMPLETED"

            );

            messagingPort.sendVideoStatus(statusMessage);
            messagingPort.sendVideoData(dataMessage);
            log.info("Video {} processed successfully", video.getId());

        } catch (Exception e) {
            log.error("Error processing video {}", video.getId(), e);
            sendErrorMessage(video);
        } finally {
            if (localFile != null) {
                try { Files.deleteIfExists(localFile); } catch (IOException ignored) {}
            }
        }
    }

    private void sendErrorMessage(Video video) {
        VideoStatusMessage errorMessage = new VideoStatusMessage(
                UUID.fromString(video.getId()),
                video.getName(),
                "ERROR",
                video.getCustomerEmail()
        );
        messagingPort.sendVideoStatus(errorMessage);
    }

    private Path saveTempFile(String videoId, byte[] videoBytes) throws IOException {
        Path tempFile = Paths.get(TEMP_DIR, videoId + ".mp4");
        Files.createDirectories(tempFile.getParent());
        Files.write(tempFile, videoBytes);
        return tempFile;
    }

    private ProcessingResult processVideo(File videoFile, String timestamp) {
        File tempDir = new File(TEMP_DIR, timestamp);
        tempDir.mkdirs();

        String framePattern = new File(tempDir, "frame_%04d.png").getAbsolutePath();
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-i", videoFile.getAbsolutePath(),
                "-vf", "fps=1", "-y", framePattern
        );

        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String error = new String(process.getErrorStream().readAllBytes());
                return new ProcessingResult(false, "Error in ffmpeg: " + error, null, 0, List.of());
            }
        } catch (Exception e) {
            return new ProcessingResult(false, "Failed to run ffmpeg: " + e.getMessage(), null, 0, List.of());
        }

        File[] frames = tempDir.listFiles((dir, name) -> name.endsWith(".png"));
        if (frames == null || frames.length == 0) {
            return new ProcessingResult(false, "No frames extracted", null, 0, List.of());
        }

        File zipFile = new File(OUTPUTS_DIR, "frames_" + timestamp + ".zip");
        try {
            createZipFile(Arrays.asList(frames), zipFile);
        } catch (IOException e) {
            return new ProcessingResult(false, "Error creating ZIP: " + e.getMessage(), null, 0, List.of());
        }

        List<String> imageNames = Arrays.stream(frames).map(File::getName).toList();

        Arrays.stream(frames).forEach(File::delete);
        tempDir.delete();

        return new ProcessingResult(true,
                "Processing completed! " + frames.length + " extracted frames.",
                zipFile.getName(), frames.length, imageNames);
    }

    private void createZipFile(List<File> files, File zipFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);
                    fis.transferTo(zos);
                    zos.closeEntry();
                }
            }
        }
    }
}
