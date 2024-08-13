package com.example.core.test.service;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.entity.BankAccount;
import com.example.core.entity.MoneyTransfer;
import com.example.core.exception.moneyTransfer.MoneyTransferCreateException;
import com.example.core.repository.BankAccountRepository;
import com.example.core.repository.MoneyTransferRepository;
import com.example.core.service.BankAccountService;
import com.example.core.service.MoneyTransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MoneyTransferServiceTest {
    @Mock
    private MoneyTransferRepository moneyTransferRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BankAccountService bankAccountService;
    @InjectMocks
    private MoneyTransferService moneyTransferService;

    @Test
    @Transactional
    public void testCreateMoneyTransfer_Success() {
        // Arrange
        ZonedDateTime fixedDateTime = ZonedDateTime.parse("2024-07-14T20:14:05.593901Z");
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setCount(100);
        moneyTransferDTO.setCurrency("USD");
        moneyTransferDTO.setDateCreate(fixedDateTime);
        moneyTransferDTO.setBankAccountFromId(1);
        moneyTransferDTO.setBankAccountToId(2);

        BankAccount bankAccountFrom = new BankAccount();
        bankAccountFrom.setId(1);
        bankAccountFrom.setCurrency("USD");

        BankAccount bankAccountTo = new BankAccount();
        bankAccountTo.setId(2);
        bankAccountTo.setCurrency("USD");

        MoneyTransfer savedMoneyTransfer = new MoneyTransfer();
        savedMoneyTransfer.setUid(new UUID(1,2));
        savedMoneyTransfer.setCount(100);
        savedMoneyTransfer.setCurrency("USD");
        savedMoneyTransfer.setDateCreate(fixedDateTime);
        savedMoneyTransfer.setBankAccountFrom(bankAccountFrom);
        savedMoneyTransfer.setBankAccountTo(bankAccountTo);

        when(bankAccountRepository.getBankAccountById(1)).thenReturn(bankAccountFrom);
        when(bankAccountRepository.getBankAccountById(2)).thenReturn(bankAccountTo);
        when(moneyTransferRepository.save(any(MoneyTransfer.class))).thenReturn(savedMoneyTransfer);

        // Act
        MoneyTransferDTO result = moneyTransferService.createMoneyTransfer(moneyTransferDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(100, result.getCount());
        assertEquals("USD", result.getCurrency());
        assertEquals(fixedDateTime, result.getDateCreate());
        assertEquals(1, result.getBankAccountFromId());
        assertEquals(2, result.getBankAccountToId());

        verify(bankAccountService).updateBalanceBankAccountById(2, 100);
        verify(bankAccountService).updateBalanceBankAccountById(1, -100);
    }

    @Test
    @Transactional
    public void testCreateMoneyTransfer_Failure_DifferentCurrencies() {
        // Arrange
        ZonedDateTime fixedDateTime = ZonedDateTime.parse("2024-07-14T20:14:05.593901Z");
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setCount(100);
        moneyTransferDTO.setCurrency("USD");
        moneyTransferDTO.setDateCreate(fixedDateTime);
        moneyTransferDTO.setBankAccountFromId(1);
        moneyTransferDTO.setBankAccountToId(2);

        BankAccount bankAccountFrom = new BankAccount();
        bankAccountFrom.setId(1);
        bankAccountFrom.setCurrency("USD");

        BankAccount bankAccountTo = new BankAccount();
        bankAccountTo.setId(2);
        bankAccountTo.setCurrency("EUR");

        when(bankAccountRepository.getBankAccountById(1)).thenReturn(bankAccountFrom);
        when(bankAccountRepository.getBankAccountById(2)).thenReturn(bankAccountTo);

        // Act & Assert
        assertThrows(MoneyTransferCreateException.class, () -> {
            moneyTransferService.createMoneyTransfer(moneyTransferDTO);
        });

        verify(moneyTransferRepository, never()).save(any(MoneyTransfer.class));
        verify(bankAccountService, never()).updateBalanceBankAccountById(anyInt(), anyInt());
    }

}
