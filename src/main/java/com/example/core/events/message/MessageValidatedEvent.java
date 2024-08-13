package com.example.core.events.message;


import com.example.core.entity.Message;
import org.springframework.context.ApplicationEvent;


public class MessageValidatedEvent extends ApplicationEvent {
    private final Message message;

    public MessageValidatedEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
