package com.example.delegats;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.MoneyTransferStatus;
import com.example.core.service.BankAccountService;
import com.example.delegats.messages.CamundaMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BalanceUpdatedDelegate implements JavaDelegate {
    private final BankAccountService bankAccountService;

    @Override
    public void execute(DelegateExecution execution) {
        CamundaMessage camundaMessage =  CamundaMessage.fromJson((String) execution.getVariable("u"));
        MoneyTransferDTO moneyTransferDTO = camundaMessage.getMoneyTransferDTO();
        try {
            bankAccountService.updateBalanceBankAccountById(moneyTransferDTO.getBankAccountToId(), moneyTransferDTO.getCount());
            bankAccountService.updateBalanceBankAccountById(moneyTransferDTO.getBankAccountFromId(), -moneyTransferDTO.getCount());
        } catch (Exception e) {
            moneyTransferDTO.setStatus(MoneyTransferStatus.REJECTED);
        }
    }
}
