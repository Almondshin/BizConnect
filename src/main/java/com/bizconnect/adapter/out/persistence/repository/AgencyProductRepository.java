package com.bizconnect.adapter.out.persistence.repository;

import com.bizconnect.adapter.out.persistence.entity.AgencyProductsJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyProductRepository extends JpaRepository<AgencyProductsJpaEntity, String> {
    Optional<AgencyProductsJpaEntity> findByRateSel(String rateSel);
}
