package com.example.core.events.message;


import com.example.core.entity.Message;
import org.springframework.context.ApplicationEvent;


public class MessageRejectedEvent extends ApplicationEvent {
    private final Message message;
    private final String reason;

    public MessageRejectedEvent(Object source, Message message, String reason) {
        super(source);
        this.message = message;
        this.reason = reason;
    }

    public Message getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }
}
