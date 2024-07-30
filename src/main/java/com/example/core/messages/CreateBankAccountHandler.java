package com.example.core.messages;


import com.example.core.dto.BankAccountDTO;
import com.example.core.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;


@Component
public class CreateBankAccountHandler implements MessageHandler {

    private final BankAccountService bankAccountService;
    private final ObjectMapper objectMapper;

    public CreateBankAccountHandler(BankAccountService bankAccountService, ObjectMapper objectMapper) {
        this.bankAccountService = bankAccountService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleMessage(Message message) throws Exception {
        BankAccountDTO bankAccountDTO = objectMapper.readValue(message.getBody(), BankAccountDTO.class);
        bankAccountService.createBankAccount(bankAccountDTO);
    }
}
