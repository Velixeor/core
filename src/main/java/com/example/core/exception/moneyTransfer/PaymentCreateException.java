package com.example.core.exception.moneyTransfer;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.exception.CoreException;


public class PaymentCreateException extends CoreException {
    public PaymentCreateException(String xml) {
        super("Failed to create payment " + xml);
    }
}
