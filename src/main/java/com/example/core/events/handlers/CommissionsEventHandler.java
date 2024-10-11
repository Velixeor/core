package com.example.core.events.handlers;


import com.example.core.dto.CommissionDTO;
import com.example.core.events.bankAccount.BalanceUpdatedEvent;
import com.example.core.service.CommissionService;
import com.example.core.service.GrpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommissionsEventHandler {
    private final ApplicationEventPublisher publisher;
    private final CommissionService commissionService;
    private final GrpcService grpcService;
    @EventListener
    public void handleCommissionSavedEvent(BalanceUpdatedEvent balanceUpdatedEvent){
        CommissionDTO commissionDTO=balanceUpdatedEvent.getCommissionDTO();
        commissionDTO.setStatus("Start");
        commissionService.createCommission(commissionDTO);



    }
}
