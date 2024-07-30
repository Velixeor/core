package com.example.core.service;


import com.example.core.dto.CoreSynchronizationDTO;
import com.example.core.messages.MessageHandler;
import com.example.core.messages.MessageHandlerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RabbitMqService {
    private final MessageHandlerFactory messageHandlerFactory;

    public RabbitMqService(MessageHandlerFactory messageHandlerFactory) {
        this.messageHandlerFactory = messageHandlerFactory;
    }

    @RabbitListener(queues = "QueuePayments")
    public void receiveMessage(Message message) {
        MessageProperties properties = message.getMessageProperties();
        String messageType = properties.getHeader("messageType");
        messageType= messageType+"Handler";
        try {
            MessageHandler handler = messageHandlerFactory.getHandler(messageType);
            if (handler != null) {
                handler.handleMessage(message);
            } else {
                log.warn("Unknown message type: {}", messageType);
            }
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }
}
