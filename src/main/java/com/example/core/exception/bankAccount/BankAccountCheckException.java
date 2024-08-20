package com.example.core.exception.bankAccount;


import com.example.core.exception.CoreException;


public class BankAccountCheckException extends CoreException {
    public BankAccountCheckException(Integer id) {
        super("Failed check bank account "  + id);
    }
}
