package com.example.core.events.moneyTransfer;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.Message;
import org.springframework.context.ApplicationEvent;


public class MoneyTransferSavedEvent extends ApplicationEvent {
    private final MoneyTransferDTO moneyTransferDTO;
    private final Message message;
    public MoneyTransferSavedEvent(Object source, MoneyTransferDTO moneyTransferDTO, Message message) {
        super(source);
        this.moneyTransferDTO = moneyTransferDTO;
        this.message = message;
    }

    public MoneyTransferDTO getMoneyTransferDTO() {
        return moneyTransferDTO;
    }
    public Message getMessage() {
        return message;
    }
}
