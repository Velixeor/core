package com.example.delegats;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.MoneyTransferStatus;
import com.example.delegats.messages.CamundaMessage;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
public class MessageUidUpdatedDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        CamundaMessage camundaMessage =  CamundaMessage.fromJson((String) execution.getVariable("mo"));
        MoneyTransferDTO moneyTransferDTO = camundaMessage.getMoneyTransferDTO();
        moneyTransferDTO.setStatus(MoneyTransferStatus.PROCESSING);
        execution.setVariable("u",  camundaMessage.toJson());

    }
}
