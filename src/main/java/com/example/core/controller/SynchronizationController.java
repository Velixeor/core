package com.example.core.controller;


import com.example.core.dto.CoreSynchronizationDTO;
import com.example.core.service.SynchronizationService;
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
@RequestMapping("/api/v1/synchronization")
@RequiredArgsConstructor
public class SynchronizationController {
    private final SynchronizationService synchronizationService;

    @PostMapping("/")
    public ResponseEntity<Void> synchronizationCreateBankAccountAndUser(@RequestBody CoreSynchronizationDTO coreSynchronizationDTO) {
        synchronizationService.startSynchronization(coreSynchronizationDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
