package com.bizconnect.adapter.Repository;

import com.bizconnect.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
