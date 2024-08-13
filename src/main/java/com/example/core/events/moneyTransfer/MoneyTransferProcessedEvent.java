package com.example.core.events.moneyTransfer;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.MoneyTransfer;
import org.springframework.context.ApplicationEvent;


public class MoneyTransferProcessedEvent extends ApplicationEvent {
    private final MoneyTransferDTO moneyTransferDTO;

    public MoneyTransferProcessedEvent(Object source, MoneyTransferDTO moneyTransferDTO) {
        super(source);
        this.moneyTransferDTO =  moneyTransferDTO;
    }
    public MoneyTransferDTO getMoneyTransferDTO() {
        return moneyTransferDTO;
    }
}
