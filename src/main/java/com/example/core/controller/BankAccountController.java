package com.example.core.controller;


import com.example.core.dto.BankAccountDTO;
import com.example.core.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@Controller
@RequestMapping("/api/v1/bank-account")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create")
    public ResponseEntity<BankAccountDTO> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        return new ResponseEntity<>(bankAccountService.createBankAccount(bankAccountDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<BankAccountDTO> updateBankAccountById(@RequestBody BankAccountDTO bankAccountDTO) {
        return new ResponseEntity<>(bankAccountService.updateBankAccountById(bankAccountDTO), HttpStatus.OK);
    }
}
