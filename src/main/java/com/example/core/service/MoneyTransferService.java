package com.example.core.service;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.MoneyTransfer;
import com.example.core.entity.MoneyTransferStatus;
import com.example.core.exception.moneyTransfer.MoneyTransferCreateException;
import com.example.core.repository.BankAccountRepository;
import com.example.core.repository.MoneyTransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;
    private final BankAccountRepository bankAccountRepository;
    @Transactional
    public MoneyTransferDTO createMoneyTransfer(MoneyTransferDTO moneyTransferDTO) {
        log.info("Start transactional");
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        transferringDataInMoneyTransferFromMoneyTransferDTO(moneyTransferDTO, moneyTransfer);
        moneyTransfer.setStatus(MoneyTransferStatus.START);
        if (moneyTransfer.getBankAccountFrom().getCurrency().equals(moneyTransfer.getBankAccountTo().getCurrency())) {
            MoneyTransfer moneyTransferResult = moneyTransferRepository.save(moneyTransfer);
            log.info("Money Transfer create successfully: {}", moneyTransferDTO);
            return transferringDataInMoneyTransferDTOFromMoneyTransfer(moneyTransferResult);
        } else {
            log.error("Failed to  create  Money Transfer: {}", moneyTransferDTO);
            throw new MoneyTransferCreateException(moneyTransferDTO);
        }
    }

    private void transferringDataInMoneyTransferFromMoneyTransferDTO(final MoneyTransferDTO moneyTransferDTO,
                                                                     MoneyTransfer moneyTransfer) {
        moneyTransfer.setUid(moneyTransferDTO.getId());
        moneyTransfer.setCount(moneyTransferDTO.getCount());
        moneyTransfer.setCurrency(moneyTransferDTO.getCurrency());
        moneyTransfer.setDateCreate(moneyTransferDTO.getDateCreate());
        moneyTransfer.setBankAccountFrom(bankAccountRepository.getBankAccountById(moneyTransferDTO.getBankAccountFromId()));
        moneyTransfer.setBankAccountTo(bankAccountRepository.getBankAccountById(moneyTransferDTO.getBankAccountToId()));
        moneyTransfer.setStatus(moneyTransferDTO.getStatus());
    }

    private MoneyTransferDTO transferringDataInMoneyTransferDTOFromMoneyTransfer(final MoneyTransfer moneyTransfer) {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setId(moneyTransfer.getUid());
        moneyTransferDTO.setCount(moneyTransfer.getCount());
        moneyTransferDTO.setDateCreate(moneyTransfer.getDateCreate());
        moneyTransferDTO.setCurrency(moneyTransfer.getCurrency());
        moneyTransferDTO.setBankAccountFromId(moneyTransfer.getBankAccountFrom().getId());
        moneyTransferDTO.setBankAccountToId(moneyTransfer.getBankAccountTo().getId());
        moneyTransferDTO.setStatus(moneyTransfer.getStatus());
        return moneyTransferDTO;
    }

}
