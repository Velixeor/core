package com.example.core.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox_commission", schema = "core")
public class Commission {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "from_whom", nullable = false)
    private Integer fromWhom;

    @Column(name = "to_whom", nullable = false)
    private Integer toWhom;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;
    @Column(name = "status", nullable = false)
    private String status;
}
