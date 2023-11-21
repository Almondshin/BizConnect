package com.bizconnect.adapter.Repository;

import com.bizconnect.domain.entity.AdditionalBizInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BizInfoRepository extends JpaRepository<AdditionalBizInfo, String> {
}
