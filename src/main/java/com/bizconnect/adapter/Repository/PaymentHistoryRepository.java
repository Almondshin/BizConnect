package com.bizconnect.adapter.Repository;

import com.bizconnect.domain.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {
}
