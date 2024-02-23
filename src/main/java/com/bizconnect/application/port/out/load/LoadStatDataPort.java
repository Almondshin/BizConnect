package com.bizconnect.application.port.out.load;

import com.bizconnect.application.domain.model.StatDay;

import java.util.List;


public interface LoadStatDataPort {
    List<StatDay> findBySiteIdAndFromDate(String siteId, String toDate, String fromDate);
}
