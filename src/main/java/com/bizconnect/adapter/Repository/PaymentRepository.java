package com.bizconnect.adapter.Repository;

import com.bizconnect.domain.entity.AdditionalPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<AdditionalPayment, String> {
}
