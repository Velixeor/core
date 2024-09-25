package com.example.core.events.bankAccount;


import com.example.core.dto.CommissionDTO;
import com.example.core.dto.MoneyTransferDTO;
import org.springframework.context.ApplicationEvent;


public class BalanceUpdatedEvent extends ApplicationEvent {
    private final CommissionDTO commissionDTO;


    public BalanceUpdatedEvent(Object source, CommissionDTO commissionDTO) {
        super(source);
        this.commissionDTO=commissionDTO;
    }

    public CommissionDTO  getCommissionDTO() {
        return commissionDTO;
    }
}
