package com.example.core.service;


import com.example.core.dto.CommissionDTO;
import com.example.core.entity.Commission;
import com.example.core.repository.CommissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommissionService {
    private final CommissionRepository commissionRepository;
    private final GrpcService grpcService;

    @Transactional
    public CommissionDTO createCommission(CommissionDTO commissionDTO) {
        log.info("Start commission creation");

        Commission commission = new Commission();
        transferringDataInCommissionFromCommissionDTO(commissionDTO, commission);

        Commission savedCommission = commissionRepository.save(commission);
        log.info("Commission created successfully: {}", commissionDTO);

        return transferringDataInCommissionDTOFromCommission(savedCommission);
    }

    //@Retryable(value = {Exception.class}, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 10800000))
    @Scheduled(fixedDelay = 10800000)
    public void retryCommissionWithDelay() {
        List<Commission> commissions = commissionRepository.findAllForUpdate();
        for(int i=0;i<commissions.size();i++){
            grpcService.sendSynchronizedBillingRequest(transferringDataInCommissionDTOFromCommission(commissions.get(i)));
        }
    }


    private void transferringDataInCommissionFromCommissionDTO(final CommissionDTO commissionDTO,
                                                               Commission commission) {
        commission.setId(commissionDTO.getId());
        commission.setFromWhom(commissionDTO.getFromWhom());
        commission.setToWhom(commissionDTO.getToWhom());
        commission.setAmount(commissionDTO.getAmount());
        commission.setCurrency(commissionDTO.getCurrency());
    }

    private CommissionDTO transferringDataInCommissionDTOFromCommission(final Commission commission) {
        CommissionDTO commissionDTO = new CommissionDTO();
        commissionDTO.setId(commission.getId());
        commissionDTO.setFromWhom(commission.getFromWhom());
        commissionDTO.setToWhom(commission.getToWhom());
        commissionDTO.setAmount(commission.getAmount());
        commissionDTO.setCurrency(commission.getCurrency());
        return commissionDTO;
    }

}
