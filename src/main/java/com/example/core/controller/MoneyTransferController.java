package com.example.core.controller;


import com.example.core.dto.MoneyTransferDTO;
import com.example.core.service.MoneyTransferService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Controller
@RequestMapping("/api/v1/money-transfer")
@RequiredArgsConstructor
public class MoneyTransferController {
    private final MoneyTransferService moneyTransferService;

    @Operation(
            summary = "Создание платежа",
            description = "Позволяет создать платеж и поменять баланс на соответствующих аккаунтов"
    )
    @PostMapping("/create")
    public ResponseEntity<MoneyTransferDTO> createMoneyTransfer(@RequestBody MoneyTransferDTO moneyTransferDTO) {
        return new ResponseEntity<>(moneyTransferService.createMoneyTransfer(moneyTransferDTO), HttpStatus.CREATED);
    }
}
