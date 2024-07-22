package com.example.core.exception.moneyTransfer;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.exception.CoreException;


public class MoneyTransferCreateException extends CoreException {
    public MoneyTransferCreateException(MoneyTransferDTO moneyTransferDTO) {
        super("Failed to create money transfer " + moneyTransferDTO);
    }
}
