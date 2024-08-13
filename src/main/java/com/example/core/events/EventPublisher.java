package com.example.core.events;


import com.example.core.events.message.MessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
public class EventPublisher {
    private final ApplicationEventPublisher publisher;

    public EventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void startProcess(String message) {
        publisher.publishEvent(new MessageEvent(this, message));
    }
}
