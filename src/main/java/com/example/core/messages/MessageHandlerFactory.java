package com.example.core.messages;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class MessageHandlerFactory {

    private final Map<String, MessageHandler> handlers = new HashMap<>();

    public MessageHandlerFactory(Map<String, MessageHandler> handlers) {
        this.handlers.putAll(handlers);
    }

    public MessageHandler getHandler(String messageType) {
        return handlers.get(messageType);
    }
}
