package com.example.core.messages;


import com.example.core.dto.CoreSynchronizationDTO;
import com.example.core.service.SynchronizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;


@Component
public class SynchronizationHandler implements MessageHandler{
    private final SynchronizationService synchronizationService;
    private final ObjectMapper objectMapper;

    public SynchronizationHandler(SynchronizationService synchronizationService, ObjectMapper objectMapper) {
        this.synchronizationService = synchronizationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleMessage(Message message) throws Exception {
        CoreSynchronizationDTO coreSynchronizationDTO = objectMapper.readValue(message.getBody(), CoreSynchronizationDTO.class);
        synchronizationService.startSynchronization(coreSynchronizationDTO);
    }
}
