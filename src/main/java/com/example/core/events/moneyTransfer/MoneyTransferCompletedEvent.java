package com.example.core.events.moneyTransfer;


import com.example.core.dto.MoneyTransferDTO;
import org.springframework.context.ApplicationEvent;


public class MoneyTransferCompletedEvent extends ApplicationEvent {
    private final MoneyTransferDTO moneyTransferDTO;

    public MoneyTransferCompletedEvent(Object source, MoneyTransferDTO moneyTransferDTO) {
        super(source);
        this.moneyTransferDTO = moneyTransferDTO;
    }

    public MoneyTransferDTO getMoneyTransferDTO() {
        return moneyTransferDTO;
    }
}
