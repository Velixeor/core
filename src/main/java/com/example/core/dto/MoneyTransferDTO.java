package com.example.core.dto;


import com.example.core.entity.MoneyTransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyTransferDTO {
    private UUID id;
    private String currency;
    private Integer count;
    private Integer bankAccountFromId;
    private Integer bankAccountToId;
    private ZonedDateTime dateCreate;
    private MoneyTransferStatus status;



}
