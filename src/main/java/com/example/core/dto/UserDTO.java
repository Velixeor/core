package com.example.core.dto;


import com.example.core.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String login;
    private Status status;
}
