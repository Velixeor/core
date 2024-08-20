package com.example.delegats;


import com.example.core.service.MoneyTransferService;
import com.example.delegats.messages.CamundaMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MoneyTransferSavedDelegate implements JavaDelegate {
    private final MoneyTransferService moneyTransferService;
    @Override
    public void execute(DelegateExecution execution) {
        CamundaMessage camundaMessage = CamundaMessage.fromJson((String) execution.getVariable("m"));
        moneyTransferService.createMoneyTransfer( camundaMessage.getMoneyTransferDTO());
        execution.setVariable("mo",  camundaMessage.toJson());
    }
}
