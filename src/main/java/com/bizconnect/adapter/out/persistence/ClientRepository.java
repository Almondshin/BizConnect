package com.bizconnect.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository  extends JpaRepository<AgencyJpaEntity, String> {
    Optional<AgencyJpaEntity> findByMallId(String mallId);
}
