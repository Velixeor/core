package com.example.core.repository;


import com.example.core.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    BankAccount getBankAccountById(Integer id);
}
