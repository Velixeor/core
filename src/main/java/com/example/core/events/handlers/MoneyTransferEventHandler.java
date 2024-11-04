package com.example.core.events.handlers;


import com.example.core.dto.CommissionDTO;
import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.Message;
import com.example.core.entity.MoneyTransferStatus;
import com.example.core.events.bankAccount.BalanceUpdatedEvent;
import com.example.core.events.message.MessageUidUpdatedEvent;
import com.example.core.events.moneyTransfer.MoneyTransferCreatedEvent;
import com.example.core.events.moneyTransfer.MoneyTransferProcessedEvent;
import com.example.core.events.moneyTransfer.MoneyTransferRejectedEvent;
import com.example.core.events.moneyTransfer.MoneyTransferSavedEvent;
import com.example.core.repository.MoneyTransferRepository;
import com.example.core.service.BankAccountService;
import com.example.core.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoneyTransferEventHandler {

    private final MoneyTransferService moneyTransferService;
    private final MoneyTransferRepository moneyTransferRepository;
    private final BankAccountService bankAccountService;
    private final ApplicationEventPublisher publisher;

    @EventListener
    public void handleMoneyTransferCreatedEvent(MoneyTransferCreatedEvent event) {
        log.debug("Create MT DTO");
        moneyTransferService.createMoneyTransfer(event.getMoneyTransferDTO());
        publisher.publishEvent(new MoneyTransferSavedEvent(this, event.getMoneyTransferDTO(), event.getMessage()));
    }

    @EventListener
    public void handleMoneyTransferSavedEvent(MoneyTransferSavedEvent event) {
        Message message = event.getMessage();
        log.debug("MT after saved: "+moneyTransferRepository.getMoneyTransferByUid(event.getMoneyTransferDTO().getId()));
        message.setMoneyTransfer(moneyTransferRepository.getMoneyTransferByUid(event.getMoneyTransferDTO().getId()));
        publisher.publishEvent(new MessageUidUpdatedEvent(this, message, event.getMoneyTransferDTO()));
    }
    @Transactional
    @EventListener
    public void handleMessageUidUpdatedEvent(MessageUidUpdatedEvent event) {
        MoneyTransferDTO moneyTransferDTO = event.getMoneyTransferDTO();
        moneyTransferDTO.setStatus(MoneyTransferStatus.PROCESSING);
        log.debug("MT DTO after update status: "+  moneyTransferDTO);
        publisher.publishEvent(new MoneyTransferProcessedEvent(this, moneyTransferDTO));
    }
    public CommissionDTO convertMoneyTransferDTOToCommissionDTO(MoneyTransferDTO moneyTransferDTO) {
        CommissionDTO commissionDTO = CommissionDTO.builder()
                .fromWhom(moneyTransferDTO.getBankAccountFromId())
                .toWhom(moneyTransferDTO.getBankAccountToId())
                .amount(BigDecimal.valueOf(moneyTransferDTO.getCount()))
                .currency(moneyTransferDTO.getCurrency())
                .build();

        return commissionDTO;
    }

    @EventListener
    public void handleMoneyTransferInProgressEvent(MoneyTransferProcessedEvent event) {
        MoneyTransferDTO moneyTransferDTO = event.getMoneyTransferDTO();
        try {
            bankAccountService.updateBalanceBankAccountById(moneyTransferDTO.getBankAccountToId(), moneyTransferDTO.getCount());
            bankAccountService.updateBalanceBankAccountById(moneyTransferDTO.getBankAccountFromId(), -moneyTransferDTO.getCount());
            log.debug("Update balance to: "+moneyTransferDTO.getBankAccountToId()+" from: "+moneyTransferDTO.getBankAccountFromId());
            publisher.publishEvent(new BalanceUpdatedEvent(this, convertMoneyTransferDTOToCommissionDTO(moneyTransferDTO)));
        } catch (Exception e) {
            moneyTransferDTO.setStatus(MoneyTransferStatus.REJECTED);
            log.debug("Failed to update: "+e);
            publisher.publishEvent(new MoneyTransferRejectedEvent(this, moneyTransferDTO, e.getMessage()));
        }
    }
}
