package com.example.core.events.handlers;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.events.DocumentUnmarshalledEvent;
import com.example.core.events.moneyTransfer.MoneyTransferCreatedEvent;
import com.example.paymentXSD.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import java.time.ZonedDateTime;
import java.util.UUID;
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentEventHandler {

    private final ApplicationEventPublisher publisher;

    @EventListener
    public void handleDocumentUnmarshalledEvent(DocumentUnmarshalledEvent event) {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        Document document = event.getDocument();

        moneyTransferDTO.setBankAccountFromId(
                Integer.valueOf(document.getFIToFICstmrCdtTrf().getGrpHdr().getSttlmInf().getThrdRmbrsmntAgtAcct().getId().getIBAN())
        );
        moneyTransferDTO.setBankAccountToId(
                Integer.valueOf(document.getFIToFICstmrCdtTrf().getGrpHdr().getSttlmInf().getSttlmAcct().getId().getIBAN())
        );
        moneyTransferDTO.setDateCreate(ZonedDateTime.now());
        moneyTransferDTO.setCurrency(document.getFIToFICstmrCdtTrf().getGrpHdr().getTtlIntrBkSttlmAmt().getCcy());
        moneyTransferDTO.setCount(document.getFIToFICstmrCdtTrf().getGrpHdr().getTtlIntrBkSttlmAmt().getValue().intValue());

        moneyTransferDTO.setId(UUID.randomUUID());
        log.debug("Successful create MT DTO");
        publisher.publishEvent(new MoneyTransferCreatedEvent(this, moneyTransferDTO, event.getMessage()));
    }
}
