package com.example.core.exception.bankAccount;


import com.example.core.exception.CoreException;


public class BankAccountUpdateBalanceException extends CoreException {
    public BankAccountUpdateBalanceException(Integer id, Integer balanceChanges) {
        super("Failed to update balance " + balanceChanges + " " + id);
    }

}
