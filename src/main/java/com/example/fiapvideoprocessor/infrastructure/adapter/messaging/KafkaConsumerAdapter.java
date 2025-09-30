package com.example.fiapvideoprocessor.infrastructure.adapter.messaging;

import com.example.fiapvideoprocessor.domain.model.Video;
import com.example.fiapvideoprocessor.domain.ports.in.VideoProcessingUseCasePort;
import com.example.fiapvideoprocessor.shared.dto.VideoProcessorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerAdapter {

    private final ObjectMapper objectMapper;
    private final VideoProcessingUseCasePort videoProcessingUseCase;

    @KafkaListener(topics = "video-processor", groupId = "video-management-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            VideoProcessorMessage message = objectMapper.readValue(record.value(), VideoProcessorMessage.class);

            log.info("Consuming message for videoId: {}", message.getVideoId());

            Video video = new Video(
                    message.getVideoId(),
                    message.getVideoName(),
                    message.getCustomerId(),
                    message.getCustomerEmail(),
                    message.getS3Key()
            );

            videoProcessingUseCase.process(video);

        } catch (Exception e) {
            log.error("Error processing video message: {}", record.value(), e);
        }
    }
}