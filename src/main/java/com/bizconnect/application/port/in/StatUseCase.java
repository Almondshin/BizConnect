package com.bizconnect.application.port.in;


import com.bizconnect.application.domain.model.StatDay;

import java.util.List;

public interface StatUseCase {
    List<StatDay> getUseCountBySiteId(String siteId, String startDate, String endDate);
}
