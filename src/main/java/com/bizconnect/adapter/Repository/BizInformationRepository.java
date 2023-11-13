package com.bizconnect.adapter.Repository;

import com.bizconnect.domain.BizInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BizInformationRepository extends JpaRepository<BizInfo, String> {
}
