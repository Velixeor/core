package com.example.delegats;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.Message;
import com.example.core.repository.MessageRepository;
import com.example.delegats.messages.CamundaMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SaveMessageDelegate implements JavaDelegate {
    private final MessageRepository messageRepository;
    @Override
    public void execute(DelegateExecution execution) {
        String xml=(String)execution.getVariable("w");

        Message message =new Message();
        message.setBody(xml);
        MoneyTransferDTO moneyTransferDTO = null;
        messageRepository.save(message);
        CamundaMessage camundaMessage = new CamundaMessage(moneyTransferDTO,message,null);
        execution.setVariable("s", camundaMessage.toJson());
    }
}
