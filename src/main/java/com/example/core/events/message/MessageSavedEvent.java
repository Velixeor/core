package com.example.core.events.message;


import com.example.core.entity.Message;
import org.springframework.context.ApplicationEvent;


public class MessageSavedEvent extends ApplicationEvent {
    private final Message message;

    public MessageSavedEvent(Object source, Message message) {
        super(source);
        this.message = message;

    }

    public Message getMessage() {
        return message;
    }
}
