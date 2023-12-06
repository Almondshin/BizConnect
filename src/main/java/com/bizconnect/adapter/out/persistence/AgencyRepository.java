package com.bizconnect.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<AgencyJpaEntity, String> {
    Optional<AgencyJpaEntity> findByAgencyIdAndMallId(String agencyId, String mallId);
    Optional<AgencyJpaEntity> findByMallId(String mallId);
}
