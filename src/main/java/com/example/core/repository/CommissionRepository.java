package com.example.core.repository;


import com.example.core.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    @Transactional
    @Query(value = "SELECT * FROM core.outbox_commission FOR UPDATE", nativeQuery = true)
    List<Commission> findAllForUpdate();
}
