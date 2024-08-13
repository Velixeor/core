package com.example.core.events.message;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.Message;
import org.springframework.context.ApplicationEvent;


public class MessageUidUpdatedEvent extends ApplicationEvent {
    private final Message message;
    private final MoneyTransferDTO moneyTransferDTO;

    public MessageUidUpdatedEvent(Object source, Message message, MoneyTransferDTO moneyTransferDTO) {
        super(source);
        this.message = message;
        this.moneyTransferDTO =  moneyTransferDTO;
    }

    public Message getMessage() {
        return message;
    }
    public MoneyTransferDTO getMoneyTransferDTO() {
        return moneyTransferDTO;
    }
}
