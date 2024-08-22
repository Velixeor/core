package com.example.core.condition;


import com.example.core.events.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessagerStarterImplEvents implements MessageStarter {
    @Autowired
    private  EventPublisher eventPublisher;

    @Override
    public void start(String xml) {
        eventPublisher.startProcess(xml);
    }
}
