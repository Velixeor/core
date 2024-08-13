package com.example.core.events.bankAccount;


import com.example.core.dto.MoneyTransferDTO;
import org.springframework.context.ApplicationEvent;


public class BalanceUpdatedEvent extends ApplicationEvent {
    private final MoneyTransferDTO moneyTransferDTO;

    public BalanceUpdatedEvent(Object source, MoneyTransferDTO moneyTransferDTO) {
        super(source);
        this.moneyTransferDTO = moneyTransferDTO;
    }

    public MoneyTransferDTO getMoneyTransferDTO() {
        return moneyTransferDTO;
    }
}
