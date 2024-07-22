package com.example.core.service;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.MoneyTransfer;
import com.example.core.exception.moneyTransfer.MoneyTransferCreateException;
import com.example.core.repository.BankAccountRepository;
import com.example.core.repository.MoneyTransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;


    public MoneyTransferService(MoneyTransferRepository moneyTransferRepository,
                                BankAccountRepository bankAccountRepository, BankAccountService bankAccountService) {
        this.moneyTransferRepository = moneyTransferRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
    }

    @Transactional
    public MoneyTransferDTO createMoneyTransfer(MoneyTransferDTO moneyTransferDTO) {
        log.info("Start transactional");
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        TransferringDataInMoneyTransferFromMoneyTransferDTO(moneyTransferDTO, moneyTransfer);
        if (moneyTransfer.getBankAccountFrom().getCurrency().equals(moneyTransfer.getBankAccountTo().getCurrency())) {
            MoneyTransfer moneyTransferResult = moneyTransferRepository.save(moneyTransfer);
            bankAccountService.updateBalanceBankAccountById(moneyTransfer.getBankAccountTo().getId(), moneyTransferDTO.getCount());
            bankAccountService.updateBalanceBankAccountById(moneyTransfer.getBankAccountFrom().getId(), -moneyTransferDTO.getCount());
            log.info("Money Transfer create successfully: {}", moneyTransferDTO);
            return TransferringDataInMoneyTransferDTOFromMoneyTransfer(moneyTransferResult);
        } else {
            log.warn("Failed to  create  Money Transfer: {}", moneyTransferDTO);
            throw new MoneyTransferCreateException(moneyTransferDTO);
        }
    }

    private void TransferringDataInMoneyTransferFromMoneyTransferDTO(MoneyTransferDTO moneyTransferDTO,
                                                                     MoneyTransfer moneyTransfer) {
        moneyTransfer.setCount(moneyTransferDTO.getCount());
        moneyTransfer.setCurrency(moneyTransferDTO.getCurrency());
        moneyTransfer.setDateCreate(moneyTransferDTO.getDateCreate());
        moneyTransfer.setBankAccountFrom(bankAccountRepository.getBankAccountById(moneyTransferDTO.getBankAccountFromId()));
        moneyTransfer.setBankAccountTo(bankAccountRepository.getBankAccountById(moneyTransferDTO.getBankAccountToId()));
    }

    private MoneyTransferDTO TransferringDataInMoneyTransferDTOFromMoneyTransfer(MoneyTransfer moneyTransfer) {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setId(moneyTransfer.getId());
        moneyTransferDTO.setCount(moneyTransfer.getCount());
        moneyTransferDTO.setDateCreate(moneyTransfer.getDateCreate());
        moneyTransferDTO.setCurrency(moneyTransfer.getCurrency());
        moneyTransferDTO.setBankAccountFromId(moneyTransfer.getBankAccountFrom().getId());
        moneyTransferDTO.setBankAccountToId(moneyTransfer.getBankAccountTo().getId());
        return moneyTransferDTO;
    }
}
