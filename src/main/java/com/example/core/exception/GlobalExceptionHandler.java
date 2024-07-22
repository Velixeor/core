package com.example.core.exception;


import com.example.core.exception.bankAccount.BankAccountUpdateBalanceException;
import com.example.core.exception.moneyTransfer.MoneyTransferCreateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MoneyTransferCreateException.class)
    public ResponseEntity<Void> handleMoneyTransferCreationException(MoneyTransferCreateException moneyTransferCreateException) {
        log.warn(moneyTransferCreateException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    @ExceptionHandler(BankAccountUpdateBalanceException.class)
    public ResponseEntity<Void> handleBankAccountUpdateBalanceException(BankAccountUpdateBalanceException bankAccountUpdateBalanceException) {
        log.warn(bankAccountUpdateBalanceException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
