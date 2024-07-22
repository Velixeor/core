package com.example.core.dto;


import com.example.core.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountDTO {
    private Integer id;
    private String code;
    private ZonedDateTime dateCreate;
    private Status status;
    private String currency;
    private Integer balance;
    private Integer userID;
}
