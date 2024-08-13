package com.example.core.events.moneyTransfer;


import com.example.core.dto.MoneyTransferDTO;
import org.springframework.context.ApplicationEvent;


public class MoneyTransferRejectedEvent extends ApplicationEvent {
    private final MoneyTransferDTO moneyTransferDTO;
    private final String reason;

    public MoneyTransferRejectedEvent(Object source, MoneyTransferDTO moneyTransferDTO, String reason) {
        super(source);
        this.moneyTransferDTO = moneyTransferDTO;
        this.reason = reason;
    }

    public MoneyTransferDTO getMoneyTransferDTO() {
        return moneyTransferDTO;
    }

    public String getReason() {
        return reason;
    }
}
