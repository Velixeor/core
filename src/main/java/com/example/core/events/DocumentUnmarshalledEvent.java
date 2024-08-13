package com.example.core.events;


import com.example.core.entity.Message;
import com.example.paymentXSD.Document;
import org.springframework.context.ApplicationEvent;


public class DocumentUnmarshalledEvent extends ApplicationEvent {
    private final Message message;
    private final Document document;

    public DocumentUnmarshalledEvent(Object source, Message message, Document document) {
        super(source);
        this.message = message;
        this.document = document;
    }

    public Message getMessage() {
        return message;
    }

    public Document getDocument() {
        return document;
    }
}
