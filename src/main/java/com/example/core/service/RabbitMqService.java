package com.example.core.service;


import com.example.core.dto.CoreSynchronizationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RabbitMqService {
    private final SynchronizationService synchronizationService;
    private final ObjectMapper objectMapper;
    public RabbitMqService(SynchronizationService synchronizationService, ObjectMapper objectMapper) {
        this.synchronizationService = synchronizationService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "QueuePayments")
    public void receiveMessage(Message message) {
        MessageProperties properties = message.getMessageProperties();
        String messageType = properties.getHeader("messageType");
        try {
            CoreSynchronizationDTO coreSynchronizationDTO = objectMapper.readValue(message.getBody(), CoreSynchronizationDTO.class);
            synchronizationService.startSynchronization(coreSynchronizationDTO);
        } catch (Exception e) {
        e.printStackTrace();
        }
    }
}
