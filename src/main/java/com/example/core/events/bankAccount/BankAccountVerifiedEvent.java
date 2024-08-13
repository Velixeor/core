package com.example.core.events.bankAccount;


import com.example.core.entity.Message;
import org.springframework.context.ApplicationEvent;


public class BankAccountVerifiedEvent extends ApplicationEvent {
    private final Message message;

    public BankAccountVerifiedEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
