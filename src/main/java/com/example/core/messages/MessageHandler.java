package com.example.core.messages;


import org.springframework.amqp.core.Message;


public interface MessageHandler {
    void handleMessage(Message message) throws Exception;
}
