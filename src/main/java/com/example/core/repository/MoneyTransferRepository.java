package com.example.core.repository;


import com.example.core.entity.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, UUID> {
    MoneyTransfer getMoneyTransferByUid(UUID uuid);

}
