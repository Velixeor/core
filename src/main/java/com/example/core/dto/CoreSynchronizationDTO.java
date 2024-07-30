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
public class CoreSynchronizationDTO  {
    private Integer bankAccountID;
    private String code;
    private ZonedDateTime dateCreate;
    private Status bankAccountStatus;
    private String currency;
    private Integer bankAccountUserId;
    private Integer userId;
    private String login;
    private Status userStatus;

}
