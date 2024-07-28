package com.example.core.dto;


import com.example.core.entity.MoneyTransfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyTransferDTO {
    private Integer id;
    private String currency;
    private Integer count;
    private Integer bankAccountFromId;
    private Integer bankAccountToId;
    private ZonedDateTime dateCreate;


}
