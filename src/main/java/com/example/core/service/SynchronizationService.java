package com.example.core.service;


import com.example.core.dto.BankAccountDTO;
import com.example.core.dto.CoreSynchronizationDTO;
import com.example.core.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class SynchronizationService {
    private final BankAccountService bankAccountService;
    private final UserService userService;

    public SynchronizationService(BankAccountService bankAccountService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.userService = userService;
    }
    @Transactional
    public void startSynchronization(CoreSynchronizationDTO coreSynchronizationDTO){
        UserDTO userDTO=new UserDTO(coreSynchronizationDTO.getUserId(), coreSynchronizationDTO.getLogin(),coreSynchronizationDTO.getUserStatus());
        BankAccountDTO bankAccountDTO= BankAccountDTO.builder()
                .id(coreSynchronizationDTO.getBankAccountID())
                .code(coreSynchronizationDTO.getCode())
                .balance(0)
                .currency(coreSynchronizationDTO.getCurrency())
                .dateCreate(coreSynchronizationDTO.getDateCreate())
                .status(coreSynchronizationDTO.getBankAccountStatus())
                .userID(coreSynchronizationDTO.getBankAccountUserId())
                .build();
        userService.createUser(userDTO);
        bankAccountService.createBankAccount(bankAccountDTO);

    }
}
