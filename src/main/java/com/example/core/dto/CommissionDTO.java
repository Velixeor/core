package com.example.core.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommissionDTO {

    private Integer id;
    private Integer fromWhom;
    private Integer toWhom;
    private BigDecimal amount;
    private String currency;
    private String status;
}
