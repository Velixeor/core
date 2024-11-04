package com.example.core.events.handlers;


import com.example.core.dto.CommissionDTO;
import com.example.core.events.bankAccount.BalanceUpdatedEvent;
import com.example.core.service.CommissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommissionsEventHandler {
    private final CommissionService commissionService;

    @EventListener
    public void handleCommissionSavedEvent(BalanceUpdatedEvent balanceUpdatedEvent) {
        CommissionDTO commissionDTO = balanceUpdatedEvent.getCommissionDTO();
        commissionDTO.setStatus("Start");
        commissionService.createCommission(commissionDTO);
        log.debug("Save commission DTO:"+commissionDTO);
    }
}
