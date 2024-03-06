package com.bizconnect.adapter.out.persistence.repository;

import com.bizconnect.adapter.out.persistence.entity.PaymentJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.querydsl.PaymentHistoryRepositoryQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentJpaEntity, String>, PaymentHistoryRepositoryQueryDSL {
    Optional<PaymentJpaEntity> findByTradeNum(String tradeNum);
}
