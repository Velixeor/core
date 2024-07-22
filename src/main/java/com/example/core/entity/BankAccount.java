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
@Table(name = "bank_account", schema = "core")
public class BankAccount {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "date_create", nullable = false)
    private ZonedDateTime dateCreate;
    @Column(name="status",nullable = false)
    private Status status;
    @Column(name = "currency", nullable = false)
    private String currency;
    @Column(name = "balance", nullable = false)
    private Integer balance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
