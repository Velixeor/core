package com.example.core.service;


import com.example.core.dto.BankAccountDTO;
import com.example.core.entity.BankAccount;
import com.example.core.exception.bankAccount.BankAccountUpdateBalanceException;
import com.example.core.repository.BankAccountRepository;
import com.example.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Transactional
    public BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = new BankAccount();
        transferringDataInBankAccountFromBankAccountDTO(bankAccountDTO, bankAccount);
        bankAccount.setBalance(0);
        BankAccount bankAccountResult = bankAccountRepository.save(bankAccount);
        return transferringDataInBankAccountDTOFromBankAccount(bankAccountResult);
    }

    public BankAccountDTO updateBankAccountById(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountRepository.getBankAccountById(bankAccountDTO.getId());
        transferringDataInBankAccountFromBankAccountDTO(bankAccountDTO, bankAccount);
        BankAccount bankAccountResult = bankAccountRepository.save(bankAccount);
        return transferringDataInBankAccountDTOFromBankAccount(bankAccountResult);
    }

    @Transactional
    public BankAccountDTO updateBalanceBankAccountById(Integer id, Integer balanceChanges) {
        log.info("Start transactional");
        BankAccount bankAccount = bankAccountRepository.getBankAccountById(id);
        bankAccount.setBalance(bankAccount.getBalance() + balanceChanges);
        if (bankAccount.getBalance() > 0) {
            BankAccount bankAccountResult = bankAccountRepository.save(bankAccount);
            BankAccountDTO bankAccountDTOResult = transferringDataInBankAccountDTOFromBankAccount(bankAccountResult);

            log.info("Bank Account update successfully: {}", id);
            return bankAccountDTOResult;
        } else {
            log.error("Failed to  update  Bank Account: {}", id);
            throw new BankAccountUpdateBalanceException(id, balanceChanges);
        }

    }

    private void transferringDataInBankAccountFromBankAccountDTO(final BankAccountDTO bankAccountDTO,
                                                                 BankAccount bankAccount) {
        bankAccount.setBalance(bankAccountDTO.getBalance());
        bankAccount.setCode(bankAccountDTO.getCode());
        bankAccount.setId(bankAccountDTO.getId());
        bankAccount.setDateCreate(bankAccountDTO.getDateCreate());
        bankAccount.setStatus(bankAccountDTO.getStatus());
        bankAccount.setUser(userRepository.getUserById(bankAccountDTO.getUserID()));
        bankAccount.setCurrency(bankAccountDTO.getCurrency());

    }

    private BankAccountDTO transferringDataInBankAccountDTOFromBankAccount(final BankAccount bankAccount) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setBalance(bankAccount.getBalance());
        bankAccountDTO.setCode(bankAccount.getCode());
        bankAccountDTO.setId(bankAccount.getId());
        bankAccountDTO.setDateCreate(bankAccount.getDateCreate());
        bankAccountDTO.setStatus(bankAccount.getStatus());
        bankAccountDTO.setUserID(bankAccount.getUser().getId());
        bankAccountDTO.setCurrency(bankAccount.getCurrency());
        return bankAccountDTO;

    }

}
