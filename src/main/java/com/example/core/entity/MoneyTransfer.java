package com.example.core.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "money_transfer", schema = "core")
public class MoneyTransfer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "currency", nullable = false)
    private String currency;
    @Column(name = "count", nullable = false)
    private Integer count;
    @Column(name = "date_create", nullable = false)
    private ZonedDateTime dateCreate;
    @ManyToOne()
    @JoinColumn(name = "from_whom", nullable = false)
    private BankAccount bankAccountFrom;
    @ManyToOne()
    @JoinColumn(name = "to_whom", nullable = false)
    private BankAccount bankAccountTo;
}
