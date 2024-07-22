package com.example.core.service;


import com.example.core.dto.BankAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class PaymentService {
    @Value("${URLPAYMENTS:http://localhost:8080/api/v1}")
    private String urlPayment;
    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Transactional
    public BankAccountDTO updateBankAccountFromPayment(BankAccountDTO bankAccountDTO) {
        String url = urlPayment + "/bank-account/update";
        log.info("Request to payment /bank-account/update: {}", bankAccountDTO);
        HttpEntity<BankAccountDTO> requestEntity = new HttpEntity<>(bankAccountDTO);
        ResponseEntity<BankAccountDTO> response = restTemplate.exchange(url,
                HttpMethod.PUT,
                requestEntity,
                BankAccountDTO.class);
        return response.getBody();
    }
}
