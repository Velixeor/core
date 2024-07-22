package com.example.core.repository;


import com.example.core.entity.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Integer> {
}
