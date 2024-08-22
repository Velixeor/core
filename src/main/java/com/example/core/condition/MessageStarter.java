package com.example.core.condition;


import org.springframework.stereotype.Component;


@Component
public interface MessageStarter {
    public void start(String xml);
}
