package com.example.core.events.commission;


import com.example.core.dto.CommissionDTO;
import org.springframework.context.ApplicationEvent;


public class CommissionSavedEvent extends ApplicationEvent {
    private final CommissionDTO commissionDTO;


    public CommissionSavedEvent(Object source, CommissionDTO commissionDTO) {
        super(source);
        this.commissionDTO = commissionDTO;
    }

    public CommissionDTO getCommissionDTO() {
        return commissionDTO;
    }


}
