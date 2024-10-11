package com.example.core.repository;


import com.example.core.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    @Transactional
    @Modifying
    @Query(value = "WITH cte AS ( " +
            "   SELECT * FROM core.outbox_commission " +
            "   WHERE status='Start' " +
            "   LIMIT 5000 " +
            "   FOR UPDATE " +
            ") " +
            "UPDATE core.outbox_commission " +
            "SET status='Processing' " +
            "WHERE id IN (SELECT id FROM cte)", nativeQuery = true)
    List<Commission> findTop5000ByStatusStart();

    void deleteAllByStatus(String status);
}
