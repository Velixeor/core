package com.example.delegats;


import com.example.core.dto.MoneyTransferDTO;
import com.example.delegats.messages.CamundaMessage;
import com.example.paymentXSD.Document;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class MoneyTransferCreatedDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        CamundaMessage camundaMessage =  CamundaMessage.fromJson((String) execution.getVariable("d"));
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        Document document = camundaMessage.getDocument();

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
        CamundaMessage moneyTransferCreatedMessage=new CamundaMessage( moneyTransferDTO,camundaMessage.getMessage(),document);
        execution.setVariable("m",  moneyTransferCreatedMessage.toJson());
    }
}
