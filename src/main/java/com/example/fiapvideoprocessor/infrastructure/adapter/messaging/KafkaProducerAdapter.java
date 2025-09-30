package com.example.fiapvideoprocessor.infrastructure.adapter.messaging;

import com.example.fiapvideoprocessor.domain.ports.out.MessagingPort;
import com.example.fiapvideoprocessor.shared.dto.VideoDataMessage;
import com.example.fiapvideoprocessor.shared.dto.VideoStatusMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerAdapter implements MessagingPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendVideoStatus(VideoStatusMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("video-status", message.getVideoId().toString(), json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing return message: {}", e.getMessage());
        }
    }

    @Override
    public void sendVideoData(VideoDataMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("video-data", message.getVideoId(), json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing return message: {}", e.getMessage());
        }
    }
}